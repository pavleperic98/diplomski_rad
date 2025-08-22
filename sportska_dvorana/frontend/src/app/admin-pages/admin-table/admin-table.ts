import { AfterViewInit, Component, Input, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { CommonModule, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../env';
import { MatInputModule } from '@angular/material/input';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ConfirmDialog, ConfirmDialogData } from '../../layout/confirm-dialog/confirm-dialog';
import { EditModal } from '../../layout/edit-modal/edit-modal';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-admin-table',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    NgFor,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSortModule,
    MatDialogModule
  ],
  templateUrl: './admin-table.html',
  styleUrls: ['./admin-table.scss']
})
export class AdminTable implements OnInit, AfterViewInit {
  @Input() title!: string;
  @Input() columns: { key: string; label: string}[] = [];
  @Input() apiEndpoint!: string;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  displayedColumns: string[] = [];
  dataSource = new MatTableDataSource<any>([]);
  searchTerm = '';

  constructor(private http: HttpClient, private dialog: MatDialog) {}

  ngOnInit() {
    this.displayedColumns = this.columns.map(c => c.key).concat('actions');
    this.loadData();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;

    this.dataSource.filterPredicate = (data, filter) => {
      const f = filter.trim().toLowerCase();
      return this.columns.some(col => {
        const v = (this.getDisplayValue(data, col.key) ?? '').toString().toLowerCase();
        return v.includes(f);
      });
    };

    this.dataSource.sortingDataAccessor = (item, property) => {
      if (property === 'dateTime') {
        return `${item.date ?? ''} ${item.timeFrom ?? ''}`.toLowerCase();
      }
      const raw = item[property];
      if (Array.isArray(raw)) {
        return raw
          .map(v => v?.sport ?? v?.hall ?? v?.name ?? '')
          .join(', ')
          .toLowerCase();
      }
      return (raw ?? '').toString().toLowerCase();
    };
  }

  ngOnChanges(changes: SimpleChanges) {
    if ((changes['apiEndpoint'] && !changes['apiEndpoint'].isFirstChange()) ||
        (changes['columns'] && !changes['columns'].isFirstChange())) {
      this.displayedColumns = this.columns.map(c => c.key).concat('actions');
      this.loadData();
    }
  }

  loadData() {
    if (!this.apiEndpoint) return;
    this.http.get<any[]>(`${environment.apiUrl}/${this.apiEndpoint}`).subscribe({
      next: (res) => {
        this.dataSource.data = res;
        if (this.paginator) this.paginator.firstPage();
      },
      error: (err) => console.error(`Greška pri učitavanju ${this.apiEndpoint}:`, err),
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
    this.dataSource.filter = filterValue;
  }

  getDisplayValue(item: any, key: string) {
    if (key === 'dateTime') {
      return `${item.date ?? ''} / ${item.timeFrom ?? ''} - ${item.timeTo ?? ''}`;
    }
    const value = item[key];
    if (Array.isArray(value)) {
      return value.map((v: any) => v?.sport ?? v?.hall ?? v?.name ?? '').join(', ');
    }
    return value ?? '/';
  }


addItem() {
  const dialogRef = this.dialog.open(EditModal, {
    width: '500px',
    data: {
      title: `Dodaj ${this.title}`,
      fields: this.columns.map(c => ({ key: c.key, label: c.label })),
      data: {},
      isEditMode: false
    }
  });

  dialogRef.afterClosed().subscribe(res => {
    if (res) {
      this.http.post(`${environment.apiUrl}/${this.apiEndpoint}`, res).subscribe({
        next: () => this.loadData(),
        error: err => console.error('Greška pri dodavanju:', err)
      });
    }
  });
}






async editItem(element: any) {

  const idCol = this.columns.find(c => c.key.toLowerCase().endsWith('id'));
  if (!idCol) {
    console.error('Nije definisana ID kolona za brisanje!');
    return;
  }
  const id = element[idCol.key];

  this.http.get(`${environment.apiUrl}/${this.apiEndpoint}/${id}`).subscribe({
    next: async (res) => {
      const fields = await this.getFieldsWithOptions(res);

      const dialogRef = this.dialog.open(EditModal, {
        width: '500px',
        data: {
          title: `Izmeni ${this.title}`,
          data: res,
          fields,
          isEditMode: true
        }
      });

      dialogRef.afterClosed().subscribe(updated => {
        if (updated) {
          this.http.put(`${environment.apiUrl}/${this.apiEndpoint}/${id}`, updated).subscribe({
            next: () => this.loadData(),
            error: err => console.error('Greška pri update:', err)
          });
        }
      });
    },
    error: err => console.error('Greška pri učitavanju itema:', err)
  });
}










  deleteItem(element: any) {
  // nađi ID kolonu
  const idCol = this.columns.find(c => c.key.toLowerCase().endsWith('id'));
  if (!idCol) {
    console.error('Nije definisana ID kolona za brisanje!');
    return;
  }
  const id = element[idCol.key];

  const dialogRef = this.dialog.open(ConfirmDialog, {
    width: '400px',
    data: {
      title: 'Potvrda brisanja',
      message: `Da li ste sigurni da želite da obrišete zapis sa ID ${id}?`,
      confirmText: 'Potvrdi',
      cancelText: 'Otkaži',
      infoOnly: false
    }
  });

  dialogRef.afterClosed().subscribe(result => {
    if (result) {
      this.http.delete(`${environment.apiUrl}/${this.apiEndpoint}/${id}`).subscribe({
        next: () => this.loadData(),
        error: err => console.error('Greška pri brisanju:', err)
      });
    }
  });
}


fieldEndpointMap: Record<string, string> = {
  roleId: 'role',
  hallId: 'hall',
  sportId: 'sport',
  statusName: 'status'
};


getFieldOptions(fieldKey: string): Promise<any[]> {
  const endpoint = this.fieldEndpointMap[fieldKey];
  if (!endpoint) return Promise.resolve([]);

  return lastValueFrom(this.http.get<any[]>(`${environment.apiUrl}/${endpoint}`));
}

async getFieldsWithOptions(data: any) {
  const fieldsWithOptions = await Promise.all(this.columns.map(async col => {

  if (['roleId', 'hallId', 'statusName'].includes(col.key)) {
  const optionsRaw = await this.getFieldOptions(col.key);

    let options;
    if (col.key === 'statusName') {
      options = optionsRaw.map((opt: any) => ({
        id: opt.statusId,       
        name: opt.status
      }));

      console.log(options);
    } else {
      // Klasičan slučaj sa id + name
      options = optionsRaw.map((opt: any) => {
        const idKey = col.key;
        const nameKey = col.key.replace('Id', '');

        return { id: opt[idKey], name: opt[nameKey] };
      });
    }

    return { ...col, options };
  }

    return col;
  }));
  return fieldsWithOptions;
}






}
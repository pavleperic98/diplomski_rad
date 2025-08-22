import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { HttpClient, HttpClientModule } from '@angular/common/http';

interface Hall {
  id: number;
  name: string;
  sportIcons: string[];
  sportTypes: string[];
  capacity: number;
  pricePerHour: number;
  images: string[];
  sports: { sport: string }[];
}

interface Sport {
  sportId: number;
  sport: string;
  hallIds: number[];
}

@Component({
  standalone: true,
  selector: 'app-hall-list',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatSelectModule,
    MatOptionModule,
    MatIconModule,
    FormsModule,
    HttpClientModule
  ],
  templateUrl: './hall-list.html',
  styleUrls: ['./hall-list.scss']
})
export class HallList {
  searchQuery = '';
  selectedSport = 'all';
  sports: string[] = ['all'];
  halls: Hall[] = [];
  filteredHalls: Hall[] = [];

  private sportIconMap: Record<string, string> = {
    'Košarka': 'sports_basketball',
    'Odbojka': 'sports_volleyball',
    'Rvanje': 'sports_martial_arts',
    'Džudo': 'sports_martial_arts',
    'Mali fudbal': 'sports_soccer',
    'Rukomet': 'sports_handball',
  };

  private hallImagesMap: Record<string, string[]> = {
    'Mala sala': ['/assets/mala-sala/1.jpg', '/assets/mala-sala/2.jpg', '/assets/mala-sala/3.jpg'],
    'Velika sala': ['/assets/velika-sala/1.jpg', '/assets/velika-sala/2.jpg', '/assets/velika-sala/3.jpg'],
    'Rvačka sala': ['/assets/rvacka-sala/1.jpg', '/assets/rvacka-sala/2.jpg', '/assets/rvacka-sala/3.jpg'],
    'Sala za džudo': ['/assets/dzudo-sala/1.jpg', '/assets/dzudo-sala/2.jpg', '/assets/dzudo-sala/3.jpg'],
  };

  constructor(private http: HttpClient, private router: Router) {
    this.loadSports();
    this.loadHalls();
  }

  loadSports(): void {
    this.http.get<Sport[]>('http://localhost:8080/api/sport').subscribe(data => {
      this.sports = data.map(s => s.sport);
    });
  }

  loadHalls(): void {
    this.http.get<any[]>('http://localhost:8080/api/hall').subscribe(data => {
      this.halls = data.map(hall => {
        const sportTypes = hall.sports?.map((s: any) => s.sport) || [];
        const sportIcons = sportTypes.map((type: string) => this.getSportIcon(type));
        const images = this.getImages(hall.name);  // ispravka ovde

        return {
          id: hall.hallId,
          name: hall.name,
          sportIcons,
          sportTypes,
          capacity: hall.capacity,
          pricePerHour: hall.pricePerHour,
          images,
          sports: hall.sports || []
        } as Hall;
      });

      this.filteredHalls = [...this.halls];
    });
  }

  applyFilters(): void {
    this.filteredHalls = this.halls.filter(hall => {
      const sportMatch = this.selectedSport === 'all' ||
        hall.sportTypes.some(type => type.toLowerCase() === this.selectedSport.toLowerCase());

      const searchMatch = !this.searchQuery ||
        hall.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        hall.sportTypes.some(type => type.toLowerCase().includes(this.searchQuery.toLowerCase()));

      return sportMatch && searchMatch;
    });
  }

  viewSlots(hall: Hall): void {
    this.router.navigate(['/halls', hall.id], {
      state: {
        sportIcons: hall.sportIcons,
        images: hall.images  
      }
    });
  }

  getSportIcon(sportName: string): string {
    return this.sportIconMap[sportName] || 'sports';
  }

  getImages(hallName: string): string[] {
    console.log(hallName);
    return this.hallImagesMap[hallName] || [];
  }
}
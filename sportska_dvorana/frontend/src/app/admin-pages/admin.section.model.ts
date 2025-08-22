export interface AdminColumn {
  key: string;
  label: string;
}

export interface AdminSection {
  label: string;
  icon?: string;
  apiEndpoint: string;
  columns: { key: string; label: string }[];
}
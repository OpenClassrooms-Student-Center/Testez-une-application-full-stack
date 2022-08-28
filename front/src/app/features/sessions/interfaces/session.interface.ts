export interface Session {
  id?: number;
  name: string;
  description: string;
  date: Date;
  teacher_id: number;
  users: number[];
  createdAt?: Date;
  updatedAt?: Date;
}

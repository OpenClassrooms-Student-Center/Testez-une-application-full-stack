export interface User {
  id: number;
  email: string;
  lastName: string;
  firstName: string;
  admin: boolean;
  password: string;
  createdAt: Date;
  updatedAt?: Date;
}

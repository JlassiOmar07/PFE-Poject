import { Exigences } from "./exigence.model";
import { Role } from "./role.model";

export class User {
    
    id!: number;
    email!: string;
    firstName!: string;
    lastName!: string;
    password!: string;
    roles!: Role[];  
    exigences!: Exigences[];
}
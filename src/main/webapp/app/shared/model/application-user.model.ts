import { type IUser } from '@/shared/model/user.model';
import { type IMensajeMuro } from '@/shared/model/mensaje-muro.model';

export interface IApplicationUser {
  id?: number;
  fechaNacimiento?: Date | null;
  telefono?: number | null;
  user?: IUser | null;
  mensajes?: IMensajeMuro[] | null;
  contactos?: IApplicationUser[] | null;
  seguidos?: IApplicationUser[] | null;
  userCS?: IApplicationUser[] | null;
  userS?: IApplicationUser[] | null;
}

export class ApplicationUser implements IApplicationUser {
  constructor(
    public id?: number,
    public fechaNacimiento?: Date | null,
    public telefono?: number | null,
    public user?: IUser | null,
    public mensajes?: IMensajeMuro[] | null,
    public contactos?: IApplicationUser[] | null,
    public seguidos?: IApplicationUser[] | null,
    public userCS?: IApplicationUser[] | null,
    public userS?: IApplicationUser[] | null,
  ) {}
}

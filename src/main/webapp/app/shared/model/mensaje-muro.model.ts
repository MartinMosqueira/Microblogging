import { type IApplicationUser } from '@/shared/model/application-user.model';

export interface IMensajeMuro {
  id?: number;
  mensaje?: string | null;
  fecha?: Date | null;
  tags?: string | null;
  user?: IApplicationUser | null;
}

export class MensajeMuro implements IMensajeMuro {
  constructor(
    public id?: number,
    public mensaje?: string | null,
    public fecha?: Date | null,
    public tags?: string | null,
    public user?: IApplicationUser | null,
  ) {}
}

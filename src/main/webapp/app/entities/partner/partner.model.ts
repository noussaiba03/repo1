export interface IPartner {
  id: number;
  codep?: string | null;
  type?: string | null;
  name?: string | null;
  contact?: string | null;
  logo?: string | null;
  logoContentType?: string | null;
  icon?: string | null;
  iconContentType?: string | null;
}

export type NewPartner = Omit<IPartner, 'id'> & { id: null };

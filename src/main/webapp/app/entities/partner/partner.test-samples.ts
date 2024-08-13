import { IPartner, NewPartner } from './partner.model';

export const sampleWithRequiredData: IPartner = {
  id: 25843,
  codep: 'ha ha sans que',
  type: 'au-dessus peut-être',
  name: 'diplomate extatique',
  contact: 'repentir comme',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
  icon: '../fake-data/blob/hipster.png',
  iconContentType: 'unknown',
};

export const sampleWithPartialData: IPartner = {
  id: 29832,
  codep: "d'avec à la faveur de commis",
  type: 'laver',
  name: 'raisonner jusqu’à ce que',
  contact: 'débile raide dîner',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
  icon: '../fake-data/blob/hipster.png',
  iconContentType: 'unknown',
};

export const sampleWithFullData: IPartner = {
  id: 16054,
  codep: 'avant que vaste personnel',
  type: 'pour que rapide à bas de',
  name: 'groin groin vu que envers',
  contact: 'pour que rapide',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
  icon: '../fake-data/blob/hipster.png',
  iconContentType: 'unknown',
};

export const sampleWithNewData: NewPartner = {
  codep: 'ouille bien',
  type: 'boum',
  name: 'chez chef intrépide',
  contact: 'oups aïe de sorte que',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
  icon: '../fake-data/blob/hipster.png',
  iconContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

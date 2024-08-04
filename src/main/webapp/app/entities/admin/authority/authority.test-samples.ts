import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '272b0411-d03d-4ab3-86eb-31b5481da75a',
};

export const sampleWithPartialData: IAuthority = {
  name: 'd704518d-e506-4041-a596-fbce71e6ef27',
};

export const sampleWithFullData: IAuthority = {
  name: '49501015-7c3d-41bb-bf3f-ce4109f99040',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

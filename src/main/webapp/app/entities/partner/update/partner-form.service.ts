import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPartner, NewPartner } from '../partner.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPartner for edit and NewPartnerFormGroupInput for create.
 */
type PartnerFormGroupInput = IPartner | PartialWithRequiredKeyOf<NewPartner>;

type PartnerFormDefaults = Pick<NewPartner, 'id'>;

type PartnerFormGroupContent = {
  id: FormControl<IPartner['id'] | NewPartner['id']>;
  codep: FormControl<IPartner['codep']>;
  type: FormControl<IPartner['type']>;
  name: FormControl<IPartner['name']>;
  contact: FormControl<IPartner['contact']>;
  logo: FormControl<IPartner['logo']>;
  logoContentType: FormControl<IPartner['logoContentType']>;
  icon: FormControl<IPartner['icon']>;
  iconContentType: FormControl<IPartner['iconContentType']>;
};

export type PartnerFormGroup = FormGroup<PartnerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PartnerFormService {
  createPartnerFormGroup(partner: PartnerFormGroupInput = { id: null }): PartnerFormGroup {
    const partnerRawValue = {
      ...this.getFormDefaults(),
      ...partner,
    };
    return new FormGroup<PartnerFormGroupContent>({
      id: new FormControl(
        { value: partnerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codep: new FormControl(partnerRawValue.codep, {
        validators: [Validators.required],
      }),
      type: new FormControl(partnerRawValue.type, {
        validators: [Validators.required],
      }),
      name: new FormControl(partnerRawValue.name, {
        validators: [Validators.required],
      }),
      contact: new FormControl(partnerRawValue.contact, {
        validators: [Validators.required],
      }),
      logo: new FormControl(partnerRawValue.logo, {
        validators: [Validators.required],
      }),
      logoContentType: new FormControl(partnerRawValue.logoContentType),
      icon: new FormControl(partnerRawValue.icon, {
        validators: [Validators.required],
      }),
      iconContentType: new FormControl(partnerRawValue.iconContentType),
    });
  }

  getPartner(form: PartnerFormGroup): IPartner | NewPartner {
    return form.getRawValue() as IPartner | NewPartner;
  }

  resetForm(form: PartnerFormGroup, partner: PartnerFormGroupInput): void {
    const partnerRawValue = { ...this.getFormDefaults(), ...partner };
    form.reset(
      {
        ...partnerRawValue,
        id: { value: partnerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PartnerFormDefaults {
    return {
      id: null,
    };
  }
}

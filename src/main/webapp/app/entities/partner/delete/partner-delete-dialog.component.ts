import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPartner } from '../partner.model';
import { PartnerService } from '../service/partner.service';

@Component({
  standalone: true,
  templateUrl: './partner-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PartnerDeleteDialogComponent {
  partner?: IPartner;

  protected partnerService = inject(PartnerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.partnerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

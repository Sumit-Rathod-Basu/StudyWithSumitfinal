package eNotesProvider.eNotesProvider.Mapper;

import org.mapstruct.Mapper;

import eNotesProvider.eNotesProvider.Dto.paymentsDto;
import eNotesProvider.eNotesProvider.Model.Payments;

@Mapper(componentModel = "spring")
public interface PaymentsConversion {
  
     paymentsDto toPaymentsDto(Payments pay);
     Payments  toPayments(paymentsDto pay);
     
}

package warranty.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import warranty.api.model.dto.ProofOfPurchaseDto;
import warranty.api.model.responses.ProofOfPurchaseResponseDto;
import warranty.api.services.ProofOfPurchaseService;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/proof-of-purchase")
public class ProofOfPurchaseController {

    private final ProofOfPurchaseService proofOfPurchaseService;

    public ProofOfPurchaseController(ProofOfPurchaseService proofOfPurchaseService) {
        this.proofOfPurchaseService = proofOfPurchaseService;
    }

    @PostMapping
    public ResponseEntity<ProofOfPurchaseResponseDto> create(@RequestBody ProofOfPurchaseDto proofOfPurchaseDto,
                                                             @AuthenticationPrincipal UserDetails userDetails) {

        return new ResponseEntity<>(proofOfPurchaseService.save(proofOfPurchaseDto, userDetails), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProofOfPurchaseResponseDto>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(proofOfPurchaseService.findAll(userDetails), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProofOfPurchaseResponseDto> getOne(@PathVariable Long id,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(proofOfPurchaseService.findOneById(id, userDetails), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ProofOfPurchaseResponseDto> update(@PathVariable Long id,
                                                             @RequestBody ProofOfPurchaseDto proofOfPurchaseDto,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(proofOfPurchaseService.update(id, proofOfPurchaseDto, userDetails), HttpStatus.OK);
    }

    // TODO implement later partial updates

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        proofOfPurchaseService.deleteById(id, userDetails);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

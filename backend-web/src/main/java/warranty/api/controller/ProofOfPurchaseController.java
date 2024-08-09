package warranty.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import warranty.api.model.ProofOfPurchase;
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
    public ResponseEntity<ProofOfPurchaseResponseDto> create(@RequestBody ProofOfPurchaseDto proofOfPurchaseDto) {
        return new ResponseEntity<>(proofOfPurchaseService.save(proofOfPurchaseDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProofOfPurchaseResponseDto>> getAll() {
        return new ResponseEntity<>(proofOfPurchaseService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProofOfPurchaseResponseDto> getOne(@PathVariable Long id) {
        return new ResponseEntity<>(proofOfPurchaseService.findOneById(id), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ProofOfPurchaseResponseDto> update(@PathVariable Long id, @RequestBody ProofOfPurchaseDto proofOfPurchaseDto) {
        return new ResponseEntity<>(proofOfPurchaseService.update(id, proofOfPurchaseDto), HttpStatus.OK);
    }

    // TODO implement later partial updates

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        proofOfPurchaseService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

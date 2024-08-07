package warranty.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import warranty.api.model.ProofOfPurchase;
import warranty.api.model.dto.ProofOfPurchaseDto;
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
    public ResponseEntity<ProofOfPurchase> create(@RequestBody ProofOfPurchaseDto proofOfPurchaseDto) {
        return new ResponseEntity<>(proofOfPurchaseService.save(proofOfPurchaseDto), HttpStatus.CREATED);
    }

//    @GetMapping
//    public ResponseEntity<List<ProofOfPurchase>> getAll() {
//        return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
//    }
//
//    @GetMapping(path = "/{id}")
//    public ResponseEntity<ProofOfPurchase> getOne(@PathVariable Long id) {
//        return new ResponseEntity<>(productService.findOne(id), HttpStatus.OK);
//    }
}

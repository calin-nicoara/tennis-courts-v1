package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/guests")
@AllArgsConstructor
public class GuestController extends BaseRestController {

    private GuestService guestService;

    @GetMapping
    public ResponseEntity<List<GuestDTO>> findGuests(@ModelAttribute GuestFilterModel guestFilterModel) {
        return ResponseEntity.ok(guestService.findGuests(guestFilterModel));
    }

    @GetMapping("/{guestId}")
    public ResponseEntity<GuestDTO> findGuestById(@PathVariable("guestId") Long guestId) {
        return ResponseEntity.ok(guestService.findGuest(guestId));
    }

    @PostMapping
    public ResponseEntity<Void> addGuest(@RequestBody @Valid GuestDTO guestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.addGuest(guestDTO).getId())).build();
    }

    @PutMapping("/{guestId}")
    public ResponseEntity<Void> updateGuest(@PathVariable("guestId") Long guestId,
                                            @RequestBody @Valid GuestDTO guestDTO) {
        guestDTO.setId(guestId);
        guestService.updateGuest(guestDTO);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{guestId}")
    public ResponseEntity<Void> deleteGuestById(@PathVariable("guestId") Long guestId) {
        guestService.deleteGuest(guestId);

        return ResponseEntity.ok().build();
    }


}

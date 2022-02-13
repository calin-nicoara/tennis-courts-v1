package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuestService {

    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    public List<GuestDTO> findGuests(GuestFilterModel guestFilterModel) {
        PageRequest pageRequest = PageRequest.of(guestFilterModel.getPage(),
                guestFilterModel.getSize(), Sort.by("id"));

        Specification<Guest> baseSpecification = Specification.where(null);

        if(StringUtils.hasText(guestFilterModel.getNameLike())) {
            baseSpecification = baseSpecification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), guestFilterModel.getNameLike().toLowerCase() + "%"));
        }

        List<Guest> guests =  guestRepository.findAll(baseSpecification, pageRequest).getContent();

        return guests.stream().map(guestMapper::map).collect(Collectors.toList());
    }

    public GuestDTO findGuest(Long guestId) {
        return guestRepository.findById(guestId).map(guestMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Guest not found.");
        });
    }

    @Transactional
    public GuestDTO addGuest(GuestDTO guestDTO) {
        Guest savedGuest = guestRepository.save(guestMapper.map(guestDTO));
        return guestMapper.map(savedGuest);
    }

    @Transactional
    public GuestDTO updateGuest(GuestDTO guestDTO) {
        if(!guestRepository.existsById(guestDTO.getId())) {
            throw new EntityNotFoundException("Guest not found.");
        }

        return addGuest(guestDTO);
    }

    @Transactional
    public void deleteGuest(Long guestId) {
        guestRepository.deleteById(guestId);
    }

}

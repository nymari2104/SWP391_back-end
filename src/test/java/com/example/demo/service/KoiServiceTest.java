package com.example.demo.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.request.koiRequest.KoiCreateRequest;
import com.example.demo.entity.Koi;
import com.example.demo.entity.Pond;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.KoiRepository;
import com.example.demo.repository.PondRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Date;

class KoiServiceTest {

    @Mock
    private KoiRepository koiRepository;

    @Mock
    private PondRepository pondRepository;

    @InjectMocks
    private KoiService koiService;

    private KoiCreateRequest koiCreateRequest;
    private Pond pond;
    private Koi koi;

    private List<Koi> list = new ArrayList<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pond = Pond.builder().pondId(193100).pondName("Pond A").build();

        koiCreateRequest = KoiCreateRequest.builder()
                .name("Koi Fsh")
                .image("image.png")
                .sex(true)
                .type("Type A")
                .origin("Japan")
                .createDate(new Date())
                .pondId(193100)
                .build();

        koi = Koi.builder()
                .name(koiCreateRequest.getName())
                .image(koiCreateRequest.getImage())
                .sex(koiCreateRequest.getSex())
                .type(koiCreateRequest.getType())
                .origin(koiCreateRequest.getOrigin())
                .createDate(koiCreateRequest.getCreateDate())
                .pond(pond)
                .build();

        list.add(Koi.builder()
                .name("Koi Fish")
                .image("image.png")
                .sex(true)
                .type("Type A")
                .origin("Japan")
                .createDate(new Date())
                .pond(pond)
                .build());

    }

    @Test
    void testCreateKoiSuccess() {

        when(pondRepository.findById(koiCreateRequest.getPondId())).thenReturn(Optional.of(pond));

        when(koiRepository.save(any(Koi.class))).thenReturn(koi);

        Koi createdKoi = koiService.createKoi(koiCreateRequest);

        assertNotNull(createdKoi);
        assertEquals("Koi Fish", createdKoi.getName());
        assertEquals(pond, createdKoi.getPond());

        verify(pondRepository, times(1)).findById(koiCreateRequest.getPondId());
        verify(koiRepository, times(1)).save(any(Koi.class));
    }

    @Test
    void testCreateKoiDuplicated() {
        Mockito.when(koiRepository.findByName(koiCreateRequest.getName()))
                .thenAnswer(invocationOnMock -> {
                    String name = invocationOnMock.getArgument(0);
                    return list.stream().anyMatch(koi1 -> koi1.getName().equals(koiCreateRequest.getName()));
                });

        when(pondRepository.findById(koiCreateRequest.getPondId())).thenReturn(Optional.of(pond));
        when(koiRepository.save(any(Koi.class))).thenReturn(koi);

        AppException exception = assertThrows(AppException.class, () -> {
            koiService.createKoi(koiCreateRequest);
        });

        // Verify the exception message
        assertEquals(ErrorCode.KOI_EXISTED, exception.getErrorCode());

    }
}

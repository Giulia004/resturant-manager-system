package com.restaurant.management.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.restaurant.management.model.Categoria;
import com.restaurant.management.model.Piatto;
import com.restaurant.management.repository.PiattoRepository;

@ExtendWith(MockitoExtension.class)
public class PiattoServiceTest {
    @Mock
    private PiattoRepository piattoRepository;

    @InjectMocks
    private PiattoService piattoService;

    private Piatto piattoSample;

    @BeforeEach
    void setUp() {
        piattoSample = new Piatto();
        piattoSample.setId(1L);
        piattoSample.setNome("Pizza Margherita");
        piattoSample.setPrezzo(6.50);
        piattoSample.setDescrizione("Pomodoro, mozzarella, basilico");
        piattoSample.setDisponibile(true);
        piattoSample.setCategoria(Categoria.PIZZE);
    }

    @Test
    void testGetAllPiatti() {
        List<Piatto> listaPiatti = Arrays.asList(piattoSample);
        when(piattoRepository.findAll()).thenReturn(listaPiatti);

        List<Piatto> result = piattoService.getAllPiatti();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pizza Margherita", result.get(0).getNome());
        verify(piattoRepository, times(1)).findAll();
    }

    @Test
    void testFindPiattoById_Success() {
        when(piattoRepository.findById(1L)).thenReturn(Optional.of(piattoSample));

        Piatto result = piattoService.findPiattoById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Pizza Margherita", result.getNome());
        verify(piattoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindPiattoById_NotFound() {
        when(piattoRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            piattoService.findPiattoById(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Il piatto non esiste", exception.getReason());
        verify(piattoRepository, times(1)).findById(99L);
    }

    @Test
    void testCreate() {
        when(piattoRepository.save(any(Piatto.class))).thenReturn(piattoSample);

        Piatto result = piattoService.create(piattoSample);

        assertNotNull(result);
        assertEquals("Pizza Margherita", result.getNome());
        verify(piattoRepository, times(1)).save(piattoSample);
    }

    @Test
    void testUpdate_Success() {
        Piatto updateDetails = new Piatto();
        updateDetails.setNome("Pizza Completa");
        updateDetails.setPrezzo(7.50);
        updateDetails.setDescrizione("Pomodoro,mozzarella,prosciutto, wrustel");
        updateDetails.setCategoria(Categoria.PIZZE);
        updateDetails.setDisponibile(true);

        when(piattoRepository.findById(1L)).thenReturn(Optional.of(piattoSample));
        when(piattoRepository.save(any(Piatto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Piatto result = piattoService.update(1L, updateDetails);

        assertNotNull(result);
        assertEquals("Pizza Completa", result.getNome());
        assertEquals(7.50, result.getPrezzo());
        verify(piattoRepository, times(1)).findById(1L);
        verify(piattoRepository, times(1)).save(piattoSample);
    }

    @Test
    void testUpdate_NotFound() {
        Piatto updateDetails = new Piatto();
        when(piattoRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> piattoService.update(99L, updateDetails));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Il piatto non esiste", exception.getReason());
        verify(piattoRepository, times(1)).findById(99L);
        verify(piattoRepository, never()).save(any(Piatto.class));
    }

    @Test
    void testDelete_Success() {
        when(piattoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(piattoRepository).deleteById(1L);

        assertDoesNotThrow(() -> piattoService.delete(1L));

        verify(piattoRepository, times(1)).existsById(1L);
        verify(piattoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(piattoRepository.existsById(99L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> piattoService.delete(99L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Il piatto non esiste", exception.getReason());
        verify(piattoRepository, times(1)).existsById(99L);
        verify(piattoRepository, never()).deleteById(99L);;
    }
}

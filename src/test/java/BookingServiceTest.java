import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gb.BookingService;
import ru.gb.CantBookException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    private final String user = "MaNi";
    private final LocalDateTime from = LocalDateTime.of(2024, 1, 9, 12, 00);
    private final LocalDateTime to = LocalDateTime.of(2024, 1, 10, 11, 59);
    private static final Logger logger
            = LoggerFactory.getLogger(BookingServiceTest.class);

    @Spy
    BookingService bookingService = new BookingService();

    @Test
    void positiveCreateBookTest() throws CantBookException {
        logger.info("positiveCreateBookTest started");
        logger.debug("Creating mock");
        Mockito.when(bookingService.checkTimeInBD(from, to)).thenReturn(true);
        Mockito.when(bookingService.createBook(user, from, to)).thenReturn(true);

        logger.debug("Running test");
        assertTrue(bookingService.book(user, from, to));

        verify(bookingService, Mockito.times(1)).checkTimeInBD(from, to);
        verify(bookingService, Mockito.times(1)).createBook(user, from, to);
        verify(bookingService).book(user, from, to);

        logger.info("positiveCreateBookTest passed");
    }

    @Test
    void negativeCreateBookTest() throws CantBookException{
        logger.info("negativeCreateBookTest started");
        logger.debug("Creating mock");
        Mockito.when(bookingService.checkTimeInBD(from, to)).thenReturn(false);
        assertThrows(CantBookException.class, () -> bookingService.book(user, from, to));
        logger.debug("Running test");
        verify(bookingService).checkTimeInBD(from, to);
        verify(bookingService, Mockito.times(1)).book(user, from, to);
        logger.info("negativeCreateBookTest passed");
    }
}

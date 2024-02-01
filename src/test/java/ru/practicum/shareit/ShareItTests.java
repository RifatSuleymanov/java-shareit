package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.business.booking.BookingController;
import ru.practicum.shareit.business.item.ItemController;
import ru.practicum.shareit.business.request.ItemRequestController;
import ru.practicum.shareit.business.user.UserController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ShareItTests {

	@Autowired
	private UserController userController;

	@Autowired
	private ItemRequestController itemRequestController;

	@Autowired
	private ItemController itemController;

	@Autowired
	private BookingController bookingController;

	@Test
	void contextLoads() {
		assertThat(userController).isNotNull();
		assertThat(itemRequestController).isNotNull();
		assertThat(itemController).isNotNull();
		assertThat(bookingController).isNotNull();
	}

}

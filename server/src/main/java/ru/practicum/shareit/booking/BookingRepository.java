package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b " +
            "from Booking b " +
            "where b.id = ?1 and (b.booker.id = ?2 or b.item.owner.id = ?2)")
    Optional<Booking> findById(long id, long bookerId);

    List<Booking> findAllByItemIdAndBookerIdAndStatusAndStartBefore(long itemId, long bookerId,
                                                                    BookingStatus status,
                                                                    LocalDateTime localDateTime);

    Optional<Booking> findByIdAndItemOwnerId(long id, long ownerId);

    List<Booking> getAllByBookerIdOrderByStartDesc(long bookerId, Pageable pageable);

    List<Booking> getAllByItemOwnerIdOrderByStartDesc(long ownerId, Pageable pageable);

    List<Booking> getAllByBookerIdAndStartAfterOrderByStartDesc(long bookerId, LocalDateTime start, Pageable pageable);

    List<Booking> getAllByItemOwnerIdAndStartAfterOrderByStartDesc(long ownerId, LocalDateTime start, Pageable pageable);

    List<Booking> getAllByBookerIdAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime start, Pageable pageable);

    List<Booking> getAllByItemOwnerIdAndEndBeforeOrderByStartDesc(long ownerId, LocalDateTime start, Pageable pageable);

    @Query("select b " +
            "from Booking b " +
            "where b.booker.id = ?1 and ?2 between b.start and b.end " +
            "ORDER BY b.start desc ")
    List<Booking> getAllByBookerIdCurrentAndBetweenTimeOrderByStartDesc(long bookerId,
                                                                        LocalDateTime time,
                                                                        Pageable pageable);

    @Query("select b " +
            "from Booking b " +
            "where b.item.owner.id = ?1 and ?2 between b.start and b.end " +
            "ORDER BY b.start desc ")
    List<Booking> getAllByOwnerIdCurrentAndBetweenTimeOrderByStartDesc(long ownerId,
                                                                       LocalDateTime time,
                                                                       Pageable pageable);

    List<Booking> getAllByBookerIdAndStatusOrderByStartDesc(long bookerId, BookingStatus status, Pageable pageable);

    List<Booking> getAllByItemOwnerIdAndStatusOrderByStartDesc(long ownerId, BookingStatus status, Pageable pageable);

    @Query("select b " +
            "from Booking b " +
            "where b.status = 'APPROVED' and b.item.id = ?1 and b.start < ?2 " +
            "order by b.end desc ")
    List<Booking> getAllByLastApprovedByItemId(long itemId, LocalDateTime dateTime);

    @Query("select b " +
            "from Booking b " +
            "where b.status = 'APPROVED' and b.item.id = ?1 and b.start > ?2 " +
            "order by b.start asc ")
    List<Booking> getAllByNextApprovedByItemId(long itemId, LocalDateTime dateTime);
}

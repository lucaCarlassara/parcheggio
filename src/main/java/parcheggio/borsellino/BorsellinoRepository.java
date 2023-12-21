package parcheggio.borsellino;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorsellinoRepository extends JpaRepository<Borsellino, Long> {
}

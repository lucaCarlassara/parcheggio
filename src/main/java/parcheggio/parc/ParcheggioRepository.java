package parcheggio.parc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcheggioRepository extends JpaRepository<Parcheggio, Long> {
}

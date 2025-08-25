package eNotesProvider.eNotesProvider.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eNotesProvider.eNotesProvider.Model.Video;
public interface VideoRepo  extends JpaRepository<Video, Long> {

}

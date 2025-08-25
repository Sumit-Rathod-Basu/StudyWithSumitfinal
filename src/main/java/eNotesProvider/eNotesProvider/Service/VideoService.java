package eNotesProvider.eNotesProvider.Service;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import eNotesProvider.eNotesProvider.Dto.VideoDto;
import eNotesProvider.eNotesProvider.Mapper.VideoConversion;
import eNotesProvider.eNotesProvider.Model.Video;
import eNotesProvider.eNotesProvider.Repository.VideoRepo;

@Service
public class VideoService {
  
@Autowired
private VideoRepo videoRepo;

@Autowired
private Cloudinary cloudinary;

@Autowired
private VideoConversion videoConversion;

     public VideoDto saveVideo(VideoDto dto, MultipartFile videoFile, MultipartFile thumbnailFile) throws IOException {
    String videoUrl = null;
    String thumbnailUrl = null;

    // Upload Video
    if (videoFile != null && !videoFile.isEmpty()) {
        String originalFilename = videoFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String publicId = UUID.randomUUID().toString() + "-video" + extension;

        Map uploadResult = cloudinary.uploader().upload(
                videoFile.getBytes(),
                ObjectUtils.asMap(
                        "folder", "videos",
                        "resource_type", "video",
                        "public_id", publicId
                )
        );

        videoUrl = uploadResult.get("secure_url").toString();
    }

    // Upload Thumbnail
    if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
        String originalFilename = thumbnailFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String publicId = UUID.randomUUID().toString() + "-thumbnail" + extension;

        Map uploadResult = cloudinary.uploader().upload(
                thumbnailFile.getBytes(),
                ObjectUtils.asMap(
                        "folder", "videos/thumbnails",
                        "resource_type", "image",   // important
                        "public_id", publicId
                )
        );

        thumbnailUrl = uploadResult.get("secure_url").toString();
    }

    // Convert DTO -> Entity
    Video video = videoConversion.toEntity(dto);
    video.setVideoUrl(videoUrl);
    video.setThumbnailUrl(thumbnailUrl);

    Video savedVideo = videoRepo.save(video);

    return videoConversion.toDto(savedVideo);
}


    public void deleteVideo(Long id) {
        videoRepo.deleteById(id);
    } 

    public java.util.List<VideoDto> getAllVideos() {
        return videoRepo.findAll().stream()
                .map(videoConversion::toDto)
                .collect(Collectors.toList());
    }

}

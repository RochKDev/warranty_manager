package warranty.api.model.dto;

/**
 * This class contains the image data.
 */
public record ImageDto(
        String name,
        String type,
        byte[] data
) {
}

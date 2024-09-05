package warranty.api.model.responses;

public record ImageResponseDto (
        Long id,
        String name,
        String type,
        byte[] data
) {
    public static ImageResponseDto fromEntity(warranty.api.model.Image image) {
        return new ImageResponseDto(
                image.getId(),
                image.getName(),
                image.getType(),
                image.getData()
        );
    }
}

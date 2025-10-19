import java.util.LocalDateTime;

public class Post{
    private int id;
    private final String name;
    private final String address;
    private final String description;
    private final String imageUrl;
    private final double price;
    private final double area;
    private final LocalDateTime startedAt;
    private LocalDateTime expiredAt;
    private PostStatus status;          // enum trạng thái: pending, approved, expired
    private int ownerId;                // userid của người đăng

    public Post(String name, String address, String description, String imageUrl, double price, double area, LocalDateTime startedAt, LocalDateTime expiredAt, int ownerId){
        this.name = name;
        this.address = address;
        this.description = descritpion;
        this.imageUrl = imageUrl;
        this.price = price;
        this.area = area;
        this.startedAt = LocalDateTime.now();
        this.
    }
}
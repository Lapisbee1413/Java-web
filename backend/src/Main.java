//package main;

import dao.PostManager;
import util.DBConnection;
import model.Post;
import model.PostStatus;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            PostManager postManager = new PostManager(conn);

            // 1. Thêm bài đăng mới (CREATE)
            Post newPost = new Post();
            newPost.setIdNguoiDang(1); // ID user có thật trong bảng users
            newPost.setTieuDe("Phòng trọ Quận 1");
            newPost.setMoTa("Phòng rộng 20m2, có gác, WC riêng");
            newPost.setDiaChiDayDu("123 Nguyễn Thái Học, Quận 1");
            newPost.setPhuongXa("Phường Phạm Ngũ Lão");
            newPost.setTinhThanhPho("TP.HCM");
            newPost.setViDo(10.123456);
            newPost.setKinhDo(106.123456);
            newPost.setGiaThang(3500000);
            newPost.setDienTichM2(20);
            newPost.setTrangThai(PostStatus.CHO_DUYET.name());
            newPost.setNgayCoTheVaoO(Date.valueOf("2025-12-01"));

            int inserted = postManager.createPost(newPost);
            System.out.println(" Thêm bài đăng mới: " + inserted);

            // 2. Lấy tất cả bài đăng (READ)
            List<Post> posts = postManager.getAllPosts();
            System.out.println("\n===== DANH SÁCH BÀI ĐĂNG =====");
            for (Post p : posts) {
                System.out.println(p.getId() + " - " + p.getTieuDe() + " - " + p.getTrangThai());
            }

            // 3. Duyệt bài (UPDATE trạng thái)
            if (!posts.isEmpty()) {
                int postId = posts.get(0).getId();
                boolean approved = postManager.changeStatus(postId);
                System.out.println("\n Duyệt bài " + postId + ": " + approved);
            }

            // 4. Xóa bài đăng
            if (!posts.isEmpty()) {
                int postId = posts.get(0).getId();
                boolean deleted = postManager.deletePost(postId, 1, true);
                System.out.println("\n Xóa bài " + postId + ": " + deleted);
            }

            // 5. Cập nhật bài hết hạn
            postManager.updateExpiredPosts();
            System.out.println("\n Đã cập nhật bài hết hạn.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

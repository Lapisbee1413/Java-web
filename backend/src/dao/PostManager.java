package dao;

import model.Post;
import model.PostStatus;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostManager {
    private final Connection conn;

    public PostManager(Connection conn) {
        this.conn = conn;
    }

     // Tạo 1 bài đăng mới. Trả về id được sinh (nếu thành công) hoặc -1 nếu thất bại.

    public int createPost(Post post) throws SQLException {
        String sql = "INSERT INTO bai_dang_cho_thue " +
                "(id_nguoi_dang, tieu_de, mo_ta, dia_chi_day_du, phuong_xa, tinh_thanhpho, vi_do, kinh_do, gia_thang, dien_tich_m2, trang_thai, ngay_co_the_vao_o) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, post.getIdNguoiDang());
            stmt.setString(2, post.getTieuDe());
            stmt.setString(3, post.getMoTa());
            stmt.setString(4, post.getDiaChiDayDu());
            stmt.setString(5, post.getPhuongXa());
            stmt.setString(6, post.getTinhThanhPho());
            stmt.setDouble(7, post.getViDo());
            stmt.setDouble(8, post.getKinhDo());
            stmt.setDouble(9, post.getGiaThang());
            stmt.setDouble(10, post.getDienTichM2());
            stmt.setString(11, post.getTrangThai()); // ví dụ "cho_duyet"

            if (post.getNgayCoTheVaoO() != null) {
                stmt.setDate(12, new java.sql.Date(post.getNgayCoTheVaoO().getTime()));
            } else {
                stmt.setNull(12, Types.DATE);
            }

            int affected = stmt.executeUpdate();
            if (affected == 0) return -1;
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    post.setId(generatedId);
                    return generatedId;
                } else {
                    return -1;
                }
            }
        }
    }

     // Lấy 1 bài theo id
    public Post getPostById(int id) throws SQLException {
        String sql = "SELECT * FROM bai_dang_cho_thue WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPost(rs);
                }
            }
        }
        return null;
    }

     // Lấy tất cả bài
    public List<Post> getAllPosts() throws SQLException {
        List<Post> list = new ArrayList<>();
        String sql = "SELECT * FROM bai_dang_cho_thue ORDER BY ngay_dang DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToPost(rs));
            }
        }
        return list;
    }


     //Lấy bài của 1 user

    public List<Post> getPostsByUser(int idNguoiDang) throws SQLException {
        List<Post> list = new ArrayList<>();
        String sql = "SELECT * FROM bai_dang_cho_thue WHERE id_nguoi_dang = ? ORDER BY ngay_dang DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idNguoiDang);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToPost(rs));
                }
            }
        }
        return list;
    }

    // Cập nhật bài (chỉ owner hoặc admin). Trả về true nếu update thành công
    public boolean updatePost(Post post, int userId, boolean isAdmin) throws SQLException {
        String sql = "UPDATE bai_dang_cho_thue SET " +
                "tieu_de = ?, mo_ta = ?, dia_chi_day_du = ?, phuong_xa = ?, tinh_thanhpho = ?, vi_do = ?, kinh_do = ?, gia_thang = ?, dien_tich_m2 = ?, trang_thai = ?, ngay_co_the_vao_o = ?, ngay_cap_nhat = CURRENT_TIMESTAMP " +
                "WHERE id = ? AND (id_nguoi_dang = ? OR ? = true)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, post.getTieuDe());
            stmt.setString(2, post.getMoTa());
            stmt.setString(3, post.getDiaChiDayDu());
            stmt.setString(4, post.getPhuongXa());
            stmt.setString(5, post.getTinhThanhPho());
            stmt.setDouble(6, post.getViDo());
            stmt.setDouble(7, post.getKinhDo());
            stmt.setDouble(8, post.getGiaThang());
            stmt.setDouble(9, post.getDienTichM2());
            stmt.setString(10, post.getTrangThai());
            if (post.getNgayCoTheVaoO() != null) {
                stmt.setDate(11, new java.sql.Date(post.getNgayCoTheVaoO().getTime()));
            } else {
                stmt.setNull(11, Types.DATE);
            }
            stmt.setInt(12, post.getId());
            stmt.setInt(13, userId);
            stmt.setBoolean(14, isAdmin);

            return stmt.executeUpdate() > 0;
        }
    }

     // Xóa bài (owner hoặc admin)
    public boolean deletePost(int postId, int userId, boolean isAdmin) throws SQLException {
        String sql = "DELETE FROM bai_dang_cho_thue WHERE id = ? AND (id_nguoi_dang = ? OR ? = true)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            stmt.setBoolean(3, isAdmin);
            return stmt.executeUpdate() > 0;
        }
    }

     // Đổi trạng thái (duyệt / từ chối / mark as rented...)
     // newStatus = ("cho_duyet","da_duyet","bi_tu_choi","da_cho_thue")
    public boolean changeStatus(int postId, String newStatus) throws SQLException {
        String sql = "UPDATE bai_dang_cho_thue SET trang_thai = ?, ngay_cap_nhat = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, postId);
            return stmt.executeUpdate() > 0;
        }
    }


     // Cập nhật trạng thái theo điều kiện (ví dụ: nếu ngay_co_the_vao_o < today thì gán là 'da_cho_thue')
    public int updateExpiredPosts() throws SQLException {
        String sql = "UPDATE bai_dang_cho_thue SET trang_thai = 'da_cho_thue', ngay_cap_nhat = CURRENT_TIMESTAMP WHERE ngay_co_the_vao_o IS NOT NULL AND ngay_co_the_vao_o <= CURDATE() AND trang_thai != 'da_cho_thue'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            return stmt.executeUpdate(); // trả về số bản ghi đã cập nhật
        }
    }

     // Helper: map ResultSet -> Post (sử dụng setters)
    private Post mapResultSetToPost(ResultSet rs) throws SQLException {
        Post p = new Post();
        p.setId(rs.getInt("id"));
        p.setIdNguoiDang(rs.getInt("id_nguoi_dang"));
        p.setTieuDe(rs.getString("tieu_de"));
        p.setMoTa(rs.getString("mo_ta"));
        p.setDiaChiDayDu(rs.getString("dia_chi_day_du"));
        p.setPhuongXa(rs.getString("phuong_xa"));
        p.setTinhThanhPho(rs.getString("tinh_thanhpho"));

        // vi_do và kinh_do có thể NULL trong DB
        double viDo = rs.getObject("vi_do") != null ? rs.getDouble("vi_do") : 0.0;
        double kinhDo = rs.getObject("kinh_do") != null ? rs.getDouble("kinh_do") : 0.0;
        p.setViDo(viDo);
        p.setKinhDo(kinhDo);

        p.setGiaThang(rs.getDouble("gia_thang"));
        p.setDienTichM2(rs.getDouble("dien_tich_m2"));
        p.setTrangThai(rs.getString("trang_thai"));

        Timestamp ngayCoThe = rs.getTimestamp("ngay_co_the_vao_o");
        if (ngayCoThe != null) p.setNgayCoTheVaoO(new Date(ngayCoThe.getTime()));
        Timestamp ngayDangTs = rs.getTimestamp("ngay_dang");
        if (ngayDangTs != null) p.setNgayDang(new Date(ngayDangTs.getTime()));
        Timestamp ngayCapNhatTs = rs.getTimestamp("ngay_cap_nhat");
        if (ngayCapNhatTs != null) p.setNgayCapNhat(new Date(ngayCapNhatTs.getTime()));

        return p;
    }
}

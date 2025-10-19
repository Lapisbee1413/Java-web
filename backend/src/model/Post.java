package model;

import java.util.Date;

public class Post{
    private int id;                     //ID của bài đăng
    private int idNguoiDang;            //userID của người đăng
    private String tieuDe;
    private String moTa;
    private String diaChiDayDu;
    private String phuongXa;
    private String tinhThanhPho;
    private double viDo;
    private double kinhDo;
    private double giaThang;
    private double dienTichM2;
    private String trangThai;
    private Date ngayCoTheVaoO;
    private Date ngayDang;
    private Date ngayCapNhat;

    public Post() {}

    public Post(int id, int idNguoiDang, String tieuDe, String moTa, String diaChiDayDu, String phuongXa, String tinhThanhPho, double viDo, double kinhDo, double giaThang, double dienTichM2, String trangThai, Date ngayCoTheVaoO, Date ngayDang, Date ngayCapNhat){
        this.id = id;
        this.idNguoiDang = idNguoiDang;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.diaChiDayDu = diaChiDayDu;
        this.phuongXa = phuongXa;
        this.tinhThanhPho = tinhThanhPho;
        this.viDo = viDo;
        this.kinhDo = kinhDo;
        this.giaThang = giaThang;
        this.dienTichM2 = dienTichM2;
        this.trangThai = trangThai;
        this.ngayCoTheVaoO = ngayCoTheVaoO;
        this.ngayDang = ngayDang;
        this.ngayCapNhat = ngayCapNhat;
    }

    //Getter và Setter cho các đối tượng
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public int getIdNguoiDang(){
        return idNguoiDang;
    }
    public void setIdNguoiDang(int idNguoiDang){
        this.idNguoiDang = idNguoiDang;
    }

    public String getTieuDe() {
        return tieuDe;
    }
    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getMoTa() {
        return moTa;
    }
    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getDiaChiDayDu() {
        return diaChiDayDu;
    }
    public void setDiaChiDayDu(String diaChiDayDu) {
        this.diaChiDayDu = diaChiDayDu;
    }

    public String getPhuongXa() {
        return phuongXa;
    }
    public void setPhuongXa(String phuongXa) {
        this.phuongXa = phuongXa;
    }

    public String getTinhThanhPho() {
        return tinhThanhPho;
    }
    public void setTinhThanhPho(String tinhThanhPho) {
        this.tinhThanhPho = tinhThanhPho;
    }

    public double getViDo() {
        return viDo;
    }
    public void setViDo(double viDo) {
        this.viDo = viDo;
    }

    public double getKinhDo() {
        return kinhDo;
    }
    public void setKinhDo(double kinhDo) {
        this.kinhDo = kinhDo;
    }

    public double getGiaThang() {
        return giaThang;
    }
    public void setGiaThang(double giaThang) {
        this.giaThang = giaThang;
    }

    public double getDienTichM2() {
        return dienTichM2;
    }
    public void setDienTichM2(double dienTichM2) {
        this.dienTichM2 = dienTichM2;
    }

    public String getTrangThai() {
        return trangThai;
    }
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Date getNgayCoTheVaoO() {
        return ngayCoTheVaoO;
    }
    public void setNgayCoTheVaoO(Date ngayCoTheVaoO) {
        this.ngayCoTheVaoO = ngayCoTheVaoO;
    }

    public Date getNgayDang() {
        return ngayDang;
    }
    public void setNgayDang(Date ngayDang) {
        this.ngayDang = ngayDang;
    }

    public Date getNgayCapNhat() {
        return ngayCapNhat;
    }
    public void setNgayCapNhat(Date ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }
}
GAME CỜ CÁ NGỰA: Một phiên bản sinh động của trò chơi bàn cổ điển Ludo, được phát triển bằng Java kết hợp thư viện Slick2D. <br>
Trò chơi hỗ trợ nhiều chế độ người chơi (Người - Máy - Không hoạt động), hoạt ảnh xúc xắc, di chuyển quân, sút quân đối phương, đưa quân về chuồng,...<br>
🚀 Tính Năng Nổi Bật <br>
    🎨 Bàn cờ Ludo sinh động, đẹp mắt <br>
    🎲 Xúc xắc quay ngẫu nhiên và hiển thị trực quan <br>
    🧠 Hỗ trợ nhiều chế độ: người chơi/người máy/không tham gia <br>
    🚗 Di chuyển, đánh bật quân đối thủ, và về đích <br>
    ⟳ Nút "Ván Mới" giúp khởi động lại trò chơi nhanh chóng <br>
    ⏱ Hiển thị luật chơi và các thông báo dễ hiểu, rõ ràng <br>
📝 Luật chơi<br>
    Bấm "Bắt đầu" ở menu để chọn chế độ cho mỗi người chơi.<br>
    Bấm "Bắt đầu" ở setup để vào giao diện play.<br>
    Nhấn "Đổ xúc xắc" để quay xúc xắc.<br>
    Xúc xắc ra 1 hoặc 6 thì được chọn một quân xuất chuồng.<br>
    Khi đã có ít nhất 1 quân trên bàn cờ thì quay đc xúc xắc bao nhiêu đi bấy nhiêu bước, không đi vào ô đầu tiên của mỗi màu quân.<br>
    Trong trường hợp đã có 1 quân trên bàn cờ thì quay được 1 hoặc 6 sẽ được quay thêm 1 lần.<br>
    Quân vào ô đã có quân đối phương sẽ đẩy họ về nhà trừ khi quân đối phương đang ở ô đầu tiên vừa xuất chuồng<br>
    Không được đi nhảy qua quân đối phương trừ khi quân đối phương đang ở ô đầu tiên vừa xuất chuồng<br>
    Ai đưa 4 quân về đích trước sẽ thắng!<br>
🚮 Cách Chạy Game<br>
KẾT QUẢ THU ĐƯỢC 

Giao diện bắt đầu 
A blue background with text and dice and a hand holding a chess piece

AI-generated content may be incorrect., Hình ảnh

5.2. Giao diện thiết lập trò chơi 

A screenshot of a computer game

AI-generated content may be incorrect., Hình ảnh 

5.3. Giao diện bàn cờ 

A screenshot of a game

AI-generated content may be incorrect., Hình ảnh 

5.4. Giao diện khi có người chiến thắng 

A screenshot of a game

AI-generated content may be incorrect., Hình ảnh 

 

5.5. Giao diện cài đặt 

A screenshot of a computer

AI-generated content may be incorrect., Hình ảnh 

5.6. Giao diện luật chơi 

A screenshot of a computer

AI-generated content may be incorrect., Hình ảnh 

5.7. Xử lý di chuyển quân 

A screenshot of a game

AI-generated content may be incorrect., Hình ảnhA game with horses and dice

AI-generated content may be incorrect., Hình ảnh 

Hình 1. Trước khi bị đá                              Hình 2 . Sau khi bị đá 

A game with horses and numbers

AI-generated content may be incorrect., Hình ảnh 

Hình 3. Khi không có nước đi khả dụng 
    Tải source code: https://github.com/kim-anh-204/VN-Ludo123.git <br>
    Yêu cầu: JDK 21 <br>
    Thư viện Slick2D + LWJGL nằm ở libs/natives/windows<br>
    Thêm thư viện thông qua VM options: -Djava.library.path="C:\VN-Ludo123\lib\natives\windows" thay "C:\VN-Ludo123\lib\natives\windows" bằng đường dẫn của bạn<br>
    Thêm jars: Project Structure->Modules->Dependencies-> click "+"-> add "jars"
    Chạy trong IDE IntelliJ <br>
    Chạy file Game.java<br>

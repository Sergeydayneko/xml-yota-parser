package model;

public class FileChatItem extends ChatItem {
    public String fileName;
    public String fileDocumentId;
    public String fileId;
    public String fileSize;
    public String fileIsDeleted;

    public FileChatItem(
        String userNick,
        String userType,
        String notice,
        String date,
        String fileName,
        String fileDocumentId,
        String fileId,
        String fileSize,
        String fileIsDeleted
    ) {
        super(userNick, userType, notice, date);
        this.fileName       = fileName;
        this.fileDocumentId = fileDocumentId;
        this.fileId         = fileId;
        this.fileSize       = fileSize;
        this.fileIsDeleted  = fileIsDeleted;
    }

    @Override
    public String toString() {
        return  "FileChatItem [userNick=" + userNick +
            ", user type=" + userType +
            ", notice=" + notice +
            ", date=" + date +
            ", filename=" + fileName +
            ", fileDocumentId=" + fileDocumentId +
            ", fileId=" + fileId +
            ", fileSize=" + fileSize +
            ", fileIsDeleted=" + fileIsDeleted + "]";
    }
}
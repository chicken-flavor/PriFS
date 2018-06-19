package pri.ayyj.fuxk.bean;

import java.util.List;

public class SinglePath {

    private List<SingleFile> Dir;
    private List<SingleFile> File;

    public List<SingleFile> getDir() {
        return Dir;
    }

    public void setDir(List<SingleFile> Dir) {
        this.Dir = Dir;
    }

    public List<SingleFile> getFile() {
        return File;
    }

    public void setFile(List<SingleFile> File) {
        this.File = File;
    }

    public static class SingleFile {
        /**
         * Type : 0
         * Name : $RECYCLE.BIN
         * FullName : f:\$RECYCLE.BIN
         * Extension :
         */

        private int Type;
        private String Name;
        private String FullName;
        private String Extension;
        private long Length;
        private boolean ReadOnly;

        public boolean MyGad;

        public int getType() {
            return Type;
        }

        public void setType(int Type) {
            this.Type = Type;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getFullName() {
            return FullName;
        }

        public void setFullName(String FullName) {
            this.FullName = FullName;
        }

        public String getExtension() {
            return Extension;
        }

        public void setExtension(String Extension) {
            this.Extension = Extension;
        }

        public long getLength() {
            return Length;
        }

        public void setLength(long length) {
            Length = length;
        }

        public boolean isReadOnly() {
            return ReadOnly;
        }

        public void setReadOnly(boolean readOnly) {
            ReadOnly = readOnly;
        }
    }
}
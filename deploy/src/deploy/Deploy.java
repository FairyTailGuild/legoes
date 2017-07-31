package deploy;

import java.io.File;
import java.util.Iterator;
import java.util.ArrayList;
import library.io.InputOutput;
import library.string.CharacterString;

public class Deploy {
        /*
         * 模块列表
         */
        private ArrayList<String> moduleList;
        /*
         * 临时目录路径
         */
        private String tmpDirPath;
        /*
         * 项目根路径
         */
        private String projectRootPath;

        /**
         * 发布blackpearl
         */
        private void deployBlackpearl() throws Exception {
                InputOutput.copyDirectory(projectRootPath + "blackpearl/bin", tmpDirPath + "blackpearl");
                InputOutput.copyDirectory(projectRootPath + "blackpearl/resource", tmpDirPath + "blackpearl/resource");
                InputOutput.compressDirectoryToJarFile(tmpDirPath + "blackpearl", tmpDirPath + "blackpearl/jar/blackpearl.jar");
                InputOutput.copyFile(tmpDirPath + "blackpearl/jar/blackpearl.jar", projectRootPath + "legoes/" + "palestink/resource/jar/blackpearl/blackpearl.jar");
                InputOutput.copyFile(tmpDirPath + "blackpearl/jar/blackpearl.jar", projectRootPath + "legoes/" + "lego/WebContent/WEB-INF/lib/blackpearl.jar");
                Iterator<String> iter = this.moduleList.iterator();
                while (iter.hasNext()) {
                        String modName = iter.next();
                        InputOutput.copyFile(tmpDirPath + "blackpearl/jar/blackpearl.jar", projectRootPath + "legoes/" + modName + "/resource/jar/blackpearl/blackpearl.jar");
                }
        }

        /**
         * 发布palestink
         */
        private void deployPalestink() throws Exception {
                InputOutput.copyDirectory(projectRootPath + "palestink/bin", tmpDirPath + "palestink");
                InputOutput.copyDirectory(projectRootPath + "palestink/resource", tmpDirPath + "palestink/resource");
                InputOutput.compressDirectoryToJarFile(tmpDirPath + "palestink", tmpDirPath + "palestink/jar/palestink.jar");
                InputOutput.copyFile(tmpDirPath + "palestink/jar/palestink.jar", projectRootPath + "legoes/" + "lego/WebContent/WEB-INF/lib/palestink.jar");
        }

        /**
         * 发布palestinkSdk
         */
        private void deployPalestinkSdk() throws Exception {
                InputOutput.copyDirectory(projectRootPath + "palestink/bin/framework/sdk", tmpDirPath + "palestinkSdk/framework/sdk");
                InputOutput.compressDirectoryToJarFile(tmpDirPath + "palestinkSdk", tmpDirPath + "palestinkSdk/jar/palestinkSdk.jar");
                Iterator<String> iter = this.moduleList.iterator();
                while (iter.hasNext()) {
                        String modName = iter.next();
                        InputOutput.copyFile(tmpDirPath + "palestinkSdk/jar/palestinkSdk.jar", projectRootPath + "legoes/" + modName + "/resource/jar/palestink/palestinkSdk.jar");
                }
        }

        /*
         * 发布lego模块
         */
        private void deployLegoModule() throws Exception {
                Iterator<String> iter = this.moduleList.iterator();
                while (iter.hasNext()) {
                        String modName = iter.next();
                        InputOutput.copyDirectory(projectRootPath + "legoes/" + modName + "/bin", tmpDirPath + modName);
                        InputOutput.copyDirectory(projectRootPath + "legoes/" + modName + "/resource", tmpDirPath + modName + "/resource");
                        InputOutput.compressDirectoryToJarFile(tmpDirPath + modName, tmpDirPath + modName + "/jar/" + modName + ".jar");
                        InputOutput.copyFile(tmpDirPath + modName + "/jar/" + modName + ".jar", projectRootPath + "legoes/" + "lego/WebContent/WEB-INF/module/" + modName + ".jar");
                }
        }

        private void clearLegoModule() throws Exception {
                String path = projectRootPath + "legoes/lego/WebContent/WEB-INF/module/";
                Iterator<String> iter = InputOutput.getCurrentDirectoryFilePath(path, null).iterator();
                while (iter.hasNext()) {
                        File f = new File(iter.next());
                        f.delete();
                }
        }

        /**
         * 初始化模块列表
         */
        private void initModuleList() {
                this.moduleList = new ArrayList<String>();
                /////////////////////////////////
                this.moduleList.add("lego_demo");
                /////////////////////////////////
                // this.moduleList.add("lego_cms");
                this.moduleList.add("lego_crm");
                this.moduleList.add("lego_user");
                this.moduleList.add("lego_fjTrade");
                this.moduleList.add("lego_storage");
                this.moduleList.add("lego_workflow");
                this.moduleList.add("lego_chinaArea");
                this.moduleList.add("lego_certificate");
                this.moduleList.add("lego_communication");
        }

        public Deploy() {
                // 按照顺序
                try {
                        // 初始化模块
                        this.initModuleList();
                        // 初始化临时目录路径
                        tmpDirPath = InputOutput.regulatePath("E:/tmp/deploy/" + CharacterString.getCurrentFormatDateTime("yyyyMMddHHmmssSSS") + "/");
                        // 创建临时目录
                        File tmpDir = new File(tmpDirPath);
                        tmpDir.mkdirs();
                        // 初始化项目根路径
                        projectRootPath = InputOutput.regulatePath(new File(Deploy.class.getResource("").getPath()).getParentFile().getParentFile().getParentFile().getParentFile().getAbsolutePath());
                        // 清理模块目录下的文件（依赖projectRootPath）
                        this.clearLegoModule();
                        // 发布库
                        this.deployBlackpearl();
                        // 发布框架
                        this.deployPalestink();
                        // 发布框架SDK
                        this.deployPalestinkSdk();
                        // 发布Lego模块
                        this.deployLegoModule();
                        // 清理临时目录
                        InputOutput.clearDir(tmpDir);
                } catch (Exception e) {
                        System.out.println(e.toString());
                }
                System.out.println("Depoly Complete!");
        }

        public static void main(String[] args) {
                new Deploy();
        }
}
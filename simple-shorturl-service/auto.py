"""
Python
自动打包，上传，重启 jar 程序
"""

from paramiko import SSHClient
from paramiko.channel import ChannelFile
from scp import SCPClient
import os

JAR_PATH = r"E:\workspace\VertX-workspace\simple-shorturl-service\target\simple-shorturl-service-1.1-SNAPSHOT-fat.jar"
DIST_PATH = "~/web-apps/"
SERVER_HOST = "weekend"
SERVER_USERNAME = "ubuntu"
SERVER_SSH_KEY_FILE = "~/.ssh/onemsg_key"
JAR_FILE = "simple-shorturl-service-1.1-SNAPSHOT-fat.jar"

# mavan 打包
def maven_package():
    os.system("mvn clean package")

def _logging(func):
    def wrapper(*args, **kwargs):
        info = "[\033[34mINFO\033[0m]"
        print("\n{} \033[33m{}\033[0m".format(
            info, "JAR 文件开始上传..."))
        
        r = func(*args)

        print(info + " ------------------------------------------------------------------------")
        print("{} \033[32m{}\033[0m".format(info, "文件上传成功"))
        print(info + " ------------------------------------------------------------------------")
        return r

    return wrapper

# 上传文件到服务器
@_logging
def upload_jar(ssh):
    scp = SCPClient(ssh.get_transport())
    scp.put(JAR_PATH, DIST_PATH)
    scp.close()


def _std2list(std: ChannelFile):
    ls = std.read().decode("utf-8").split("\n")
    ls.remove("")
    return ls

# 打印 jps 输出
def jps(ssh):
    stdin, stdout, stderr = ssh.exec_command("/apps/jdk-11.0.5/bin/jps")
    stdout: ChannelFile
    stdout = _std2list(stdout)

    print("[\033[34mSTDOUT\033[0m] {}".format(stdout))
    return stdout

# 关闭服务器 jps 进程
def stop_jar(ssh):

    stdout = jps(ssh)
    pid = None
    # 关闭 java 进程
    for line in stdout:
        if JAR_FILE in line:
            pid = line.split()[0]
            ssh.exec_command("kill -15 {}".format(pid))
    if pid != None:
        print("pid {} 已关闭".format(pid))

    jps(ssh)


# 启动服务
def start_jar(ssh):

    ssh.exec_command("cd web-apps; source run.sh")
    print("jar 已重新启动")
    jps(ssh)
    # stdout = ssh.exec_command("tail web-apps/shorturl-service.log")[1]
    # print(stdout.read().decode("utf-8"))


if __name__ == "__main__":
    
    ssh = SSHClient()
    ssh.load_system_host_keys()
    ssh.connect(SERVER_HOST, username=SERVER_USERNAME,
                key_filename=SERVER_SSH_KEY_FILE)
    
    maven_package()
    upload_jar(ssh)
    stop_jar(ssh)
    start_jar(ssh)

    ssh.close()


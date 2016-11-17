#!/bin/sh
USER_DIR="/home/sakai"

export LANG="zh_CN.UTF-8"
export LC_ALL="zh_CN.UTF-8"

export JAVA_HOME="$USER_DIR"/jdk1.8

export CATALINA_HOME="$USER_DIR"/tomcat8

export PATH=$JAVA_HOME/bin:$CATALINA_HOME/bin:$PATH:$HOME/bin


#JAVA_OPTS="-server -Xmx1024m -XX:MaxNewSize=256m -XX:MaxPermSize=512m -Dhttp.agent=Sakai -Dsakai.demo=true -Djava.awt.headless=true -Dorg.apache.jasper.compiler.Parser.STRICT_QUOTE_ESCAPING=false -Dorg.apache.jasper.compiler.Parser.STRICT_WHITESPACE=false -Dsun.lang.ClassLoader.allowArraySyntax=true  -Dfile.encoding=utf-8 -Dsun.jnu.encoding=utf-8"
JAVA_OPTS="-server -Xmx2024m -XX:MaxNewSize=256m -XX:MaxPermSize=512m -Dhttp.agent=Sakai -Djava.awt.headless=true -Dorg.apache.jasper.compiler.Parser.STRICT_QUOTE_ESCAPING=false -Dorg.apache.jasper.compiler.Parser.STRICT_WHITESPACE=false -Dsun.lang.ClassLoader.allowArraySyntax=true  -Dfile.encoding=utf-8 -Dsun.jnu.encoding=utf-8 -Duser.region=CN"
export JAVA_OPTS

#CATALINA_OPTS="-server -Xmx1024m -XX:MaxNewSize=256m -XX:MaxPermSize=512m -Dhttp.agent=Sakai -Dsakai.demo=true -Djava.awt.headless=true -Dorg.apache.jasper.compiler.Parser.STRICT_QUOTE_ESCAPING=false -Dorg.apache.jasper.compiler.Parser.STRICT_WHITESPACE=false -Dsun.lang.ClassLoader.allowArraySyntax=true  -Dfile.encoding=utf-8 -Dsun.jnu.encoding=utf-8"
CATALINA_OPTS="-server -Xmx2024m -XX:MaxNewSize=256m -XX:MaxPermSize=512m -Dhttp.agent=Sakai -Djava.awt.headless=true -Dorg.apache.jasper.compiler.Parser.STRICT_QUOTE_ESCAPING=false -Dorg.apache.jasper.compiler.Parser.STRICT_WHITESPACE=false -Dsun.lang.ClassLoader.allowArraySyntax=true  -Dfile.encoding=utf-8 -Dsun.jnu.encoding=utf-8 -Duser.region=CN"
export CATALINA_OPTS

# Demo failed to start correctly if CATALINA_HOME / CATALINA_BASE is set SAK-11553
unset CATALINA_HOME CATALINA_BASE 

bin/startup.sh; tail -f logs/catalina.out

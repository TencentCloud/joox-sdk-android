# Notice: If you want apply this template to your project,
#         replace the 'XXX' in the script


# Init environment
export ANDROID_HOME=$ANDROID_SDK
export JAVA_HOME=$JDK8
export GRADLE_HOME=/data/rdm/apps/gradle/gradle-4.6
export PATH=$JDK8/bin:$GRADLE_HOME/bin:$PATH

#备份文件
name="${BaseLine}_r${GIT_COMMIT:0:8}_`date '+%Y%m%d%H%M%S'`"

# Build
gradle build

# Copy results
cp app/build/outputs/apk/release/jooxsdk.apk bin/${name}_demo.apk
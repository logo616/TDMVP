# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.

cmake_minimum_required(VERSION 3.4.1)

# Specifies a library name, specifies whether the library is STATIC or
# SHARED, and provides relative paths to the source code. You can
# define multiple libraries by adding multiple add.library() commands,
# and CMake builds them for you. When you build your app, Gradle
# automatically packages shared libraries with your APK.


#设置生成的so动态库最后输出的路径
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI})


#设置头文件搜索路径（和此txt同个路径的头文件无需设置），可选
#INCLUDE_DIRECTORIES(${CMAKE_CURRENT_SOURCE_DIR}/common)

#指定用到的系统库或者NDK库或者第三方库的搜索路径，可选。
#LINK_DIRECTORIES(/usr/local/lib)


# add_library( # Specifies the name of the library.
            # SignUtil

            # # Sets the library as a shared library.
            # SHARED

            # # Provides a relative path to your source file(s).
            # src/main/jni/sign.cpp )


#当前cmakelists.txt所在目录的所有.c .cpp源文件
#ADD_SUBDIRECTORY( src/main/jni/ )
AUX_SOURCE_DIRECTORY(src/main/jni/ SRC_LIST)

#增加其他目录的源文件到集合变量中
#list(APPEND SRC_LIST
#        ../common/1.c
#        ../common/2.c
#    )

#生成so库，直接使用变量代表那些.c文件集合
add_library(SignUtil SHARED ${SRC_LIST})

# Specifies a path to native header files.

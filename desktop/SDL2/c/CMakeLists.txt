cmake_minimum_required(VERSION 3.8)
project(jni_sdl2)


#set(CMAKE_CXX_STANDARD 11)
set(CMAKE_C_STANDARD 99)

include_directories(depends/include/ ../../../minijvm/c/jvm)

set(SOURCE_FILES
        media.h
        jni_sdl2.c
        jni_mir.c
        media.c
        )
#add_executable(test_sdl2 ${SOURCE_FILES})

add_library(jni_mir SHARED ${SOURCE_FILES})


#find_package(OpenGL REQUIRED)
MESSAGE(STATUS "CMAKE_SYSTEM :${CMAKE_SYSTEM}")
MESSAGE(STATUS "CMAKE_SYSTEM_NAME :${CMAKE_SYSTEM_NAME}")
MESSAGE(STATUS "CMAKE_SYSTEM_PROCESSOR :${CMAKE_SYSTEM_PROCESSOR}")
MESSAGE(STATUS "WIN32 :${WIN32}")
MESSAGE(STATUS "CMAKE_GENERATOR :${CMAKE_GENERATOR}")
MESSAGE(STATUS "CMAKE_SIZEOF_VOID_P  :${CMAKE_SIZEOF_VOID_P}")
MESSAGE(STATUS "CMAKE_SOURCE_DIR  :${CMAKE_SOURCE_DIR}")


IF (APPLE)
    MESSAGE("apple os here(not supported yet).")
    find_library(SDL2_LIB libsdl2.a depends/lib/mac_x64/)
    MARK_AS_ADVANCED(SDL2_LIB)
    SET(EXTRA_LIBS ${SDL2_LIB})

    TARGET_LINK_LIBRARIES(sdl2 ${EXTRA_LIBS})
    TARGET_LINK_LIBRARIES(test_sdl2 ${EXTRA_LIBS})
    SET(LIBRARY_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/../../../binary/mac_x64)
ELSEIF (UNIX)
    MESSAGE("linux os here(not supported yet).")
    find_library(SDL2_LIB libsdl2.a "${CMAKE_SOURCE_DIR}/depends/lib/centos_x64/")
    MARK_AS_ADVANCED(SDL2_LIB)
    TARGET_LINK_LIBRARIES(sdl2 ${SDL2_LIB})
    TARGET_LINK_LIBRARIES(sdl2 pthread)
    SET(LIBRARY_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/../../../binary/centos_x64)
ELSEIF (MINGW)
    IF (CMAKE_SIZEOF_VOID_P EQUAL 4)
        message("32bit")
        find_library(SDL2_LIB libsdl2.a "${CMAKE_SOURCE_DIR}/depends/lib/win_i686/")
        SET(LIBRARY_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/../../../binary/win32)
        SET(DEPLIBS_SOURCE_PATH ${CMAKE_SOURCE_DIR}/depends/lib/win_i686)
    elseif (CMAKE_SIZEOF_VOID_P EQUAL 8)
        message("64bit")
        find_library(SDL2_LIB SDL2.dll "${CMAKE_SOURCE_DIR}/depends/lib/win_x64/")
        find_library(SDL2_IMG_LIB SDL2_image.dll "${CMAKE_SOURCE_DIR}/depends/lib/win_x64/")
        SET(LIBRARY_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/../../../binary/win_x64)
        SET(DEPLIBS_SOURCE_PATH ${CMAKE_SOURCE_DIR}/depends/lib/win_x64)
    endif ()
    message(${SDL2_LIB})
    MARK_AS_ADVANCED(SDL2_LIB)
    TARGET_LINK_LIBRARIES(jni_mir PUBLIC ${SDL2_LIB} PUBLIC ${SDL2_IMG_LIB}
                PRIVATE winmm
                PRIVATE imm32
                PRIVATE setupapi
                PRIVATE version)

    #all: TARGET_LINK_LIBRARIES(jni_mir kernel32 user32 gdi32 winmm imm32 ole32 oleaut32 version uuid advapi32 setupapi shell32)

    #TARGET_LINK_LIBRARIES(test_sdl2 winmm imm32)
    #TARGET_LINK_LIBRARIES(test_sdl2 ${SDL2_LIB})
    set(CMAKE_EXE_LINKER_FLAGS "-mwindows")

    #copy SDL2 and SDL2_image dll to target
    set(OUTPUT_DIR ${PROJECT_SOURCE_DIR}/../../../binary/win_x64)

    add_custom_command(
        OUTPUT ${OUTPUT_DIR}/SDL2.dll
        COMMAND ${CMAKE_COMMAND} -E copy_if_different
            ${DEPLIBS_SOURCE_PATH}/SDL2.dll
            ${OUTPUT_DIR}/SDL2.dll
        DEPENDS jni_mir
        COMMENT "Copying file SDL2.dll to output directory"
    )
    add_custom_target(copy_sdl_dll ALL DEPENDS ${OUTPUT_DIR}/SDL2.dll)

    add_custom_command(
        OUTPUT ${OUTPUT_DIR}/SDL2_image.dll
        COMMAND ${CMAKE_COMMAND} -E copy_if_different
            ${DEPLIBS_SOURCE_PATH}/SDL2_image.dll
            ${OUTPUT_DIR}/SDL2_image.dll
        DEPENDS jni_mir
        COMMENT "Copying file SDL2_image.dll to output directory"
    )
    add_custom_target(copy_sdl_img_dlls ALL DEPENDS ${OUTPUT_DIR}/SDL2_image.dll)

ENDIF ()

message(${LIBRARY_OUTPUT_PATH})



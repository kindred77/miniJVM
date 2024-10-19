//
// Created by Gust on 2018/2/1.
//


#include "jvm_util.h"
#include "jvm.h"
#include "media.h"


void JNI_OnLoad(MiniJVM *jvm) {
    printf("---JNI_OnLoad---000--");
    memset(&refers, 0, sizeof(GlobeRefer));
    JniEnv *env = jvm->env;
    refers.jvm = jvm;
    refers.env = env;

    refers.runtime_list = env->pairlist_create(10);
    env->native_reg_lib(jvm, ptr_SDLFuncTable(), count_SDL2FuncTable());
}

void JNI_OnUnload(MiniJVM *jvm) {
    JniEnv *env = jvm->env;
    env->native_remove_lib(jvm, ptr_SDLFuncTable());
}

Runtime *getRuntimeCurThread(JniEnv *env) {
    if (env->get_jvm_state(refers.jvm) != JVM_STATUS_RUNNING) {
        return NULL;
    }
    thrd_t t = env->thrd_current();
    Runtime *runtime = env->pairlist_get(refers.runtime_list, (__refer) (intptr_t) t);
    if (!runtime) {
        runtime = env->runtime_create(refers.jvm);
        env->thread_boundle(runtime);
        env->jthread_set_daemon_value(runtime->thrd_info->jthread, runtime, 1);
        env->pairlist_put(refers.runtime_list, (__refer) (intptr_t) t, runtime);
    }

    return env->getLastSon(runtime);//
}

/* ===============================================================
 *
 *                          DEMO
 *
 * ===============================================================*/

int main(void) {

    return 0;
}

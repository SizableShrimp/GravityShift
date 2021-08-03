function initializeCoreMod() {
    var Opcodes = Java.type("org.objectweb.asm.Opcodes");
    var ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI");

    return {
        'replace_player_superclasses': {
            'target': {
                'type': 'CLASS',
                'names': function(listOfClasses) {
                    return ['net.minecraft.client.entity.player.ClientPlayerEntity', 'net.minecraft.entity.player.ServerPlayerEntity']
                }
            },
            'transformer': function(classNode) {
                classNode.superName = 'me.sizableshrimp.gravityshift.entity.GravityAwarePlayer';

                return classNode;
            }
        }
    }
}
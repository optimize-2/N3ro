package cn.n3ro.ghostclient.hooks;

import cn.n3ro.ghostclient.Client;
import cn.n3ro.ghostclient.events.EventPacket;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.types.EventType;
import jdk.nashorn.internal.objects.NativeError;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class NetworkManagerHook implements Opcodes {

    public static void transformNetworkManager(ClassNode classNode, MethodNode methodNode) {
        if (methodNode.name.equalsIgnoreCase("scheduleOutboundPacket") || methodNode.name.equalsIgnoreCase("func_150725_a")){
            final InsnList preInsn = new InsnList();
            preInsn.add(new VarInsnNode(Opcodes.ALOAD, 1));//方法的第一个参数
            preInsn.add(new FieldInsnNode(Opcodes.GETSTATIC, "com/darkmagician6/eventapi/types/EventType", "SEND", "Lcom/darkmagician6/eventapi/types/EventType;"));
            preInsn.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(NetworkManagerHook.class), "channelRead0Hook","(Ljava/lang/Object;Lcom/darkmagician6/eventapi/types/EventType;)Z", false));
            final LabelNode jmp = new LabelNode();
            preInsn.add(new JumpInsnNode(Opcodes.IFEQ, jmp));
            preInsn.add(new InsnNode(Opcodes.RETURN));
            preInsn.add(jmp);
            preInsn.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
            methodNode.instructions.insert(preInsn);
        }
        if (methodNode.name.equalsIgnoreCase("channelRead0") && methodNode.desc.equalsIgnoreCase("(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V")){
            final InsnList preInsn = new InsnList();
            preInsn.add(new VarInsnNode(Opcodes.ALOAD, 2));
            preInsn.add(new FieldInsnNode(Opcodes.GETSTATIC, "com/darkmagician6/eventapi/types/EventType", "RECIEVE", "Lcom/darkmagician6/eventapi/types/EventType;"));
            preInsn.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(NetworkManagerHook.class), "channelRead0Hook","(Ljava/lang/Object;Lcom/darkmagician6/eventapi/types/EventType;)Z", false));
            final LabelNode jmp = new LabelNode();
            preInsn.add(new JumpInsnNode(Opcodes.IFEQ, jmp));
            preInsn.add(new InsnNode(Opcodes.RETURN));
            preInsn.add(jmp);
            preInsn.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
            methodNode.instructions.insert(preInsn);
            /**
             * if(channelRead0Hook(packet,EventType.RECIEVE)){
             *  return ;
             * }
             */
        }
    }

    public static boolean channelRead0Hook(Object packet, EventType eventType) {
        if(packet != null) {
            if (packet instanceof S05PacketSpawnPosition){
                Client.canCancle = false;
            }
            final EventPacket event = new EventPacket(eventType,packet);
            EventManager.call(event);
            if (event.getPacket() instanceof S08PacketPlayerPosLook){
                Client.canCancle = true;
            }
            return event.isCancelled();
        }
        return false;
    }
}

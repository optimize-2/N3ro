package cn.n3ro.ghostclient.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;

public class ClassNodeUtils {
	public static ClassNode toClassNode(byte[] bytes ) {
		ClassReader classReader = new ClassReader(bytes);
		ClassNode classNode = new ClassNode();
		classReader.accept(classNode, 0);

		return classNode;
	}


	public static byte[] toBytes(ClassNode classNode) {
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(classWriter);

		return classWriter.toByteArray();
	}
	
	public static void writeFile(ClassNode classNode, String pathPre) throws FileNotFoundException, IOException {
		String path = classNode.name.replace(".", "\\");
		path = pathPre + path + ".class";
		try {
			
			File file = new File(path);
			if(!file.exists()) {
				new File(file.getParent()).mkdirs();
				
//				file.createNewFile();
			}
				
			FileOutputStream fileOutputStream = new FileOutputStream(path);
			fileOutputStream.write(toBytes(classNode));
		}
		catch (FileNotFoundException e) {
			throw e;
		}
		catch (IOException e) {
			throw e;
		}
	}
	
	public static InsnList toNodes(AbstractInsnNode... nodes) {
		InsnList insnList = new InsnList();
		for(AbstractInsnNode node : nodes)
			insnList.add(node);
		return insnList;
	}
}
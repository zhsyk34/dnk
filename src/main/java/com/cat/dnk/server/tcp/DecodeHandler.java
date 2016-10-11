package com.cat.dnk.server.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class DecodeHandler extends ByteToMessageDecoder {

	private static int getIntByBytes(byte[] bytes) {
		if (bytes.length > 4) {
			throw new RuntimeException();
		}


	}

	public static String binaryToHexString(byte... bytes){
		String hexStr =  "0123456789ABCDEF";
		String result = "";
		String hex = "";
		for(int i=0;i<bytes.length;i++){
			//字节高4位
			hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));
			//字节低4位
			hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));
			result +=hex;
		}
		return result;
	}

	public static int getInfo(byte... values) {
		String hexStr = binaryToHexString(values);
		return Integer.parseInt(hexStr, 16);
	}
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() >= 8) {
			byte header1 = in.readByte();
			byte header2 = in.readByte();

			// 判断是否是5A A5开头，每个完整包都是这样开头
			if (header1 == (byte) 0x5A && header2 == (byte) 0xA5) {
				// 2字节+数据长度+2个校验字节
				dataLength = ProcotolUtils.getInfo(new byte[]{receDatas.get(2), receDatas.get(3)});
				if (receDatas.size() < dataLength + 4) {
					logger.warn("数据只是接收开头部分");
					return;
				}
				if (dataLength > 4) {
					// 校验是否有正常结尾数据
					int endByte1 = receDatas.get(dataLength + 2);
					int endByte2 = receDatas.get(dataLength + 3);
					if (endByte1 == -91 && endByte2 == 90) {
						// 正常结尾
						checkData = new byte[dataLength - 2];
						TcpUtil.copyData(checkData, receDatas, 2);
						if (TcpUtil.checkData(checkData, new byte[]{receDatas.get(dataLength), receDatas.get(dataLength + 1)})) {
							String data = TcpUtil.getRealData(checkData);
							// logger.info("数据校验通过");
							// 校验通过，移除正常完整包段
							// receDatas = new
							// ArrayList<Byte>(receDatas.subList(dataLength + 4,
							// receDatas.size()));
							// receDatas = new
							// CopyOnWriteArrayList<Byte>(receDatas.subList(dataLength
							// + 4, receDatas.size()));
							receDatas.removeAll(receDatas.subList(0, dataLength + 4));
							// receDatas.subList(dataLength + 4,receDatas.size());
							dataProcessing(data);
						} else {
							receDatas.clear();
							logger.warn("数据校验不通过");
						}
					}

				}

			} else {
				// 开头不完整数据异常掉
				int eb1 = receDatas.indexOf(new Byte((byte) 0xA5));

				if (eb1 >= 0 && receDatas.size() > eb1 + 2) {
					Byte eb2Byte = receDatas.get(eb1 + 1);
					if (eb2Byte == 0x5A) {
						// receDatas = new
						// CopyOnWriteArrayList<Byte>(receDatas.subList(eb1 + 2,
						// receDatas.size()));
						receDatas.removeAll(receDatas.subList(0, dataLength + 4));
						logger.warn("数据开头不完整，被移除了");
					}
				} else {
					logger.warn("发送数据内部格式有误，清空缓存");
					receDatas.clear();
				}

			}
		}
	}
}

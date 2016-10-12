package com.cat.dnk.server.tcp;

import com.cat.dnk.data.CodecKit;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.cat.dnk.data.EncoderKit.validateVerify;
import static com.cat.dnk.other.EncoderKit2.test;

public class TCPDecodeHandler extends ByteToMessageDecoder {

	private static final Logger LOGGER = LoggerFactory.getLogger(TCPDecodeHandler.class);

	//TODO
	private static int search(ByteBuf buf, byte[] bytes) {
		int begin = buf.indexOf(0, buf.readableBytes(), bytes[0]);

		if (begin == -1) {
			return -1;
		}

		begin++;
		for (int i = 1; i < bytes.length - 1; i++) {
			if (buf.getByte(begin) == bytes[1]) {

			}
		}

		return -1;

	}

	private static final byte[] HEADER = new byte[]{0x5A, (byte) 0xA5};
	//TODO test header
//	private static final byte[] HEADER = new byte[]{97, 98};
	private static final byte[] FOOTER = new byte[]{(byte) 0xA5, 0x5A};

	private static int searchHeader(ByteBuf buf, int fromIndex) {
		int index = buf.indexOf(fromIndex, buf.readableBytes(), HEADER[0]);
		if (index == -1 || index == buf.readableBytes() - 2) {
			return -1;
		}
		return buf.getByte(index + 1) == HEADER[1] ? index : -1;
	}

	//header=2+length=2+data>3+verify=2+footer=2
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		//logger
		LOGGER.info("decode data[" + in.readableBytes() + "]:");
		for (int i = in.readerIndex(); i < in.readerIndex() + in.readableBytes(); i++) {
			System.out.print(in.getByte(i) + " ");
		}
		System.out.println();

		if (in.readableBytes() < 11) {
			LOGGER.warn("等待数据中...数据至少应有11位");
			return;
		}

		//header
		int index = in.indexOf(in.readerIndex(), in.readableBytes(), HEADER[0]);
		if (index == -1) {
			in.clear();
			LOGGER.warn("没有匹配到合法数据,清除已接收到的数据");
			return;
		}

		LOGGER.warn("匹配到第一个帧头位置:" + index);
		in.readerIndex(index + 1);

		//footer index is index+10 at least
		if (in.readableBytes() <= 10) {
			LOGGER.warn("数据不完整(粗略估计),继续等待中...");
			return;
		}
		if (in.readByte() != HEADER[1]) {
			LOGGER.warn("第二个帧头数据不匹配,丢弃此前数据:");
			return;
		}

		LOGGER.warn("匹配到第二个帧头位置:" + (index + 1));

		//length
		byte[] lengthArr = new byte[]{in.readByte(), in.readByte()};
		int length = CodecKit.byteArrayToInt(lengthArr);
		LOGGER.info("校验长度:" + length + ",数据长度:" + (length - 4));
		if (length <= 4) {
			LOGGER.error("错误的长度校验数据");
			LOGGER.info("重置指针到[" + index + "]下个位置:[" + (index + 1) + "]");
			in.readerIndex(index + 1);
			return;
		}

		if (in.readableBytes() < length) {
			LOGGER.warn("数据不完整(校验长度),继续等待中...");
			in.readerIndex(index);
			return;
		}

		//data
		byte[] data = in.readBytes(length - 4).array();

		//verify
		byte[] verifyArr = new byte[]{in.readByte(), in.readByte()};
		if (validateVerify(data, verifyArr)) {
			LOGGER.warn("校验值错误");
			return;
		}

		//footer
		if (in.readByte() == FOOTER[0] && in.readByte() == FOOTER[1]) {
			LOGGER.info("获取数据:" + new String(data));
			return;
		}
		LOGGER.info("重置指针到" + index + "下一个");
		in.readerIndex(index + 1);

//		// 1.header:5A A5
//		if (header1 == (byte) 0x5A && header2 == (byte) 0xA5) {
//			//2.剩余部分应有长度
//			byte[] lengthByteArray = new byte[]{in.getByte(2), in.getByte(3)};
//			int length = CodecKit.byteArrayToInt(lengthByteArray);
//
//			if (in.readableBytes() < length) {
//				System.err.println("剩余数据不完整");
//			}
//
//			//3.数据正文长度
//			int dataLength = length - 4;
//			in.skipBytes(dataLength);
//
//			//4.verify
//			byte[] verifyByteArray = new byte[]{in.readByte(), in.readByte()};
//
//			byte footer1 = in.readByte();
//			byte footer2 = in.readByte();
//
//			//5.footer
//			if (footer1 == (byte) 0xA5 && footer2 == (byte) 0x5A) {
//				out.add(Unpooled.wrappedBuffer(in));
//			}
//		}
	}

	public static void main(String[] args) {
//		String s = "{}";
//		System.out.println(s.getBytes().length);
		byte[] bs = new byte[]{0x11, 0x22, 0x33, 0x44, 0x5a, (byte) 0xa5, (byte) 0x99};
		ByteBuf buf = Unpooled.copiedBuffer(bs);

		byte[] array = buf.readBytes(3).array();
		test(array);

		System.out.println(buf.readerIndex());

//		int index = buf.indexOf(0, buf.readableBytes(), (byte) 0x5a);
//		System.out.println(index);
//
//		int i = buf.forEachByte(new ByteBufProcessor() {
//			@Override
//			public boolean process(byte value) throws Exception {
//				return value != 0x5a;
//			}
//		});
//
//		System.out.println(i);

	}
}

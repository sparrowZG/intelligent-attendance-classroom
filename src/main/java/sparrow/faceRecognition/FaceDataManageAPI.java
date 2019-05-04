package sparrow.faceRecognition;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import sparrow.AidaiduUtil.Base64Util;
import sparrow.AidaiduUtil.FileUtil;
import sparrow.AidaiduUtil.HttpUtil;
import sparrow.service.AuthService;

import java.util.ArrayList;
import java.util.List;

public class FaceDataManageAPI {
	private static final String FACE_MATCH = "https://aip.baidubce.com/rest/2.0/face/v3/match";
	public static void main(String[] args) {
		String accessToken = AuthService.getAuth();
		System.out.println("----------------");
		System.out.println(accessToken);
		String result = FaceMatch("C:\\githubProject\\intelligent-attendance-classroom\\src\\main\\resource\\png\\test1.jpg",
			"C:\\githubProject\\intelligent-attendance-classroom\\src\\main\\resource\\png\\test1.jpg", accessToken);

		FaceV3MatchBean faceV3MatchBean = JSONObject.toJavaObject(JSON.parseObject(result), FaceV3MatchBean.class);
		System.out.println("对比分数值:"+faceV3MatchBean.getResult().getScore());
	}
	/**
	 * 人脸对比示例代码
	 * @param path1 图片本地路径1
	 * @param path2 图片本地路径1
	 * @param token AccessToken
	 * @return
	 */
	public static String FaceMatch(String path1,String path2,String token) {
		try {
			// 本地文件路径
			String filePath1 = path1;
			String filePath2 = path2;
			byte[] imgData1 = FileUtil.readFileByBytes(filePath1);
			byte[] imgData2 = FileUtil.readFileByBytes(filePath2);
			String imgStr1 = Base64Util.encode(imgData1);
			String imgStr2 = Base64Util.encode(imgData2);
			List<FaceV3Bean> faceMatchs = new ArrayList<FaceV3Bean>();
			FaceV3Bean faceMatch1 = new FaceV3Bean(imgStr1,"BASE64");
			FaceV3Bean faceMatch2 = new FaceV3Bean(imgStr2,"BASE64");
			faceMatchs.add(faceMatch1);
			faceMatchs.add(faceMatch2);
			String param = JSONObject.toJSONString(faceMatchs);
			System.out.println("======"+param);
			// 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
			String accessToken = token;
			String result = HttpUtil.post(FACE_MATCH, accessToken, param);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

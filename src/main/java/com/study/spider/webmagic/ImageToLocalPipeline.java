package com.study.spider.webmagic;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

public class ImageToLocalPipeline extends FilePersistentBase implements Pipeline {

	public ImageToLocalPipeline(String path)  {
        setPath(path);
    }
	
	@Override
	public void process(ResultItems resultItems, Task task) {
		// TODO Auto-generated method stub
		String imageStr = resultItems.get("imageStr");
        String path = this.path + task.getUUID() + "/";
        checkAndMakeParentDirecotry(path);
        boolean saveSucess = ImageBase64Utils.GenerateImage(path, "a.jpg", imageStr);
        if (saveSucess) {
            System.out.println("==================================成功");
        }
	}

}


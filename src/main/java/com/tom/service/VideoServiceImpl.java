package com.tom.service;

import com.tom.dao.VideoDao;
import com.tom.pojo.Video;
import com.tom.pojo.vo.DescVideo;
import com.tom.pojo.vo.PlayVideo;
import com.tom.pojo.vo.QueryVideo;
import com.tom.pojo.vo.UploadVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Tom
 * @create: 2020-11-04 19:39
 * @description:
 **/
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    VideoDao videoDao;

    @Override
    public int addInfoAboutVideo(UploadVideo uploadVideo) {
        return videoDao.addInfoAboutVideo(uploadVideo);
    }

    @Override
    public List<QueryVideo> queryAllVideo() {
        return videoDao.queryAllVideo();
    }

    @Override
    public int deleteByVideoId(String videoId) {
        return videoDao.deleteByVideoId(videoId);
    }

    @Override
    public Video queryByVideoId(String videoId) {
        return videoDao.queryByVideoId(videoId);
    }

    @Override
    public List<DescVideo> queryIndexVideo() {
        return videoDao.queryIndexVideo();
    }

    @Override
    public PlayVideo queryByCover(String cover) {
        return videoDao.queryByCover(cover);
    }
}





























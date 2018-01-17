package com.yc.junior.english.read.model.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by admin on 2017/7/26.
 * 教材
 */

@Entity
public class BookInfo implements MultiItemEntity {

    public static final int CLICK_ITEM_VIEW = 1;

    @Id
    private Long id;

    private String bookId;

    private String name;

    @JSONField(name = "cover_img")
    private String coverImg;

    @JSONField(name = "version_id")
    private String versionId;

    private String period;

    @JSONField(name = "part_type")
    private String partType;

    private String grade;

    private String subject;

    @JSONField(name = "is_del")
    private String isDel;

    private String sort;

    private String press;

    @JSONField(name = "sentence_count")
    private String sentenceCount;

    private String gradeName;

    private String versionName;

    public int Type = CLICK_ITEM_VIEW;

    public BookInfo(final int type) {
        Type = type;
    }

    @Generated(hash = 421121061)
    public BookInfo(Long id, String bookId, String name, String coverImg,
            String versionId, String period, String partType, String grade,
            String subject, String isDel, String sort, String press,
            String sentenceCount, String gradeName, String versionName, int Type) {
        this.id = id;
        this.bookId = bookId;
        this.name = name;
        this.coverImg = coverImg;
        this.versionId = versionId;
        this.period = period;
        this.partType = partType;
        this.grade = grade;
        this.subject = subject;
        this.isDel = isDel;
        this.sort = sort;
        this.press = press;
        this.sentenceCount = sentenceCount;
        this.gradeName = gradeName;
        this.versionName = versionName;
        this.Type = Type;
    }

    @Generated(hash = 1952025412)
    public BookInfo() {
    }

    @Override
    public int getItemType() {
        return Type;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return this.Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public String getBookId() {
        return this.bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverImg() {
        return this.coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getVersionId() {
        return this.versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getPeriod() {
        return this.period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPartType() {
        return this.partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIsDel() {
        return this.isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getSort() {
        return this.sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSentenceCount() {
        return this.sentenceCount;
    }

    public void setSentenceCount(String sentenceCount) {
        this.sentenceCount = sentenceCount;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}

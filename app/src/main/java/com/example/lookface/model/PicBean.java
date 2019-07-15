package com.example.lookface.model;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class PicBean extends LitePalSupport {

    /**
     * image_id : O2alrpeRIXFejHWe6WlRqw==
     * request_id : 1562820005,cb604e61-544f-49ea-be9a-a8b2f9cfad01
     * time_used : 785
     * faces : [{"attributes":{"emotion":{"sadness":0.385,"neutral":73.459,"disgust":0.004,"anger":0.004,"surprise":25.761,"fear":0.004,"happiness":0.381},"gender":{"value":"Female"},"age":{"value":24},"smile":{"threshold":50,"value":0.104},"facequality":{"threshold":70.1,"value":0.006},"ethnicity":{"value":"WHITE"},"beauty":{"female_score":89.59,"male_score":87.166}},"face_token":"4f7be3c18509c1f36a7e0bcb8c254cbc"}]
     */

    private String image_id;
    private String request_id;
    private int time_used;
    private List<FacesBean> faces;

    private String path;

    private String GSONString;

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public List<FacesBean> getFaces() {
        return faces;
    }

    public void setFaces(List<FacesBean> faces) {
        this.faces = faces;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGSONString() {
        return GSONString;
    }

    public void setGSONString(String GSONString) {
        this.GSONString = GSONString;
    }

    public static class FacesBean {
        /**
         * attributes : {"emotion":{"sadness":0.385,"neutral":73.459,"disgust":0.004,"anger":0.004,"surprise":25.761,"fear":0.004,"happiness":0.381},"gender":{"value":"Female"},"age":{"value":24},"smile":{"threshold":50,"value":0.104},"facequality":{"threshold":70.1,"value":0.006},"ethnicity":{"value":"WHITE"},"beauty":{"female_score":89.59,"male_score":87.166}}
         * face_token : 4f7be3c18509c1f36a7e0bcb8c254cbc
         */

        private AttributesBean attributes;
        private String face_token;


        public AttributesBean getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributesBean attributes) {
            this.attributes = attributes;
        }

        public String getFace_token() {
            return face_token;
        }

        public void setFace_token(String face_token) {
            this.face_token = face_token;
        }

        public static class AttributesBean {
            /**
             * emotion : {"sadness":0.385,"neutral":73.459,"disgust":0.004,"anger":0.004,"surprise":25.761,"fear":0.004,"happiness":0.381}
             * gender : {"value":"Female"}
             * age : {"value":24}
             * smile : {"threshold":50,"value":0.104}
             * facequality : {"threshold":70.1,"value":0.006}
             * ethnicity : {"value":"WHITE"}
             * beauty : {"female_score":89.59,"male_score":87.166}
             */

            //lite pal

            private EmotionBean emotion;
            private GenderBean gender;
            private AgeBean age;
            private SmileBean smile;
            private FacequalityBean facequality;
            private EthnicityBean ethnicity;
            private BeautyBean beauty;

            public EmotionBean getEmotion() {
                return emotion;
            }

            public void setEmotion(EmotionBean emotion) {
                this.emotion = emotion;
            }

            public GenderBean getGender() {
                return gender;
            }

            public void setGender(GenderBean gender) {
                this.gender = gender;
            }

            public AgeBean getAge() {
                return age;
            }

            public void setAge(AgeBean age) {
                this.age = age;
            }

            public SmileBean getSmile() {
                return smile;
            }

            public void setSmile(SmileBean smile) {
                this.smile = smile;
            }

            public FacequalityBean getFacequality() {
                return facequality;
            }

            public void setFacequality(FacequalityBean facequality) {
                this.facequality = facequality;
            }

            public EthnicityBean getEthnicity() {
                return ethnicity;
            }

            public void setEthnicity(EthnicityBean ethnicity) {
                this.ethnicity = ethnicity;
            }

            public BeautyBean getBeauty() {
                return beauty;
            }

            public void setBeauty(BeautyBean beauty) {
                this.beauty = beauty;
            }

            public static class EmotionBean {
                /**
                 * sadness : 0.385
                 * neutral : 73.459
                 * disgust : 0.004
                 * anger : 0.004
                 * surprise : 25.761
                 * fear : 0.004
                 * happiness : 0.381
                 */

                private double sadness;
                private double neutral;
                private double disgust;
                private double anger;
                private double surprise;
                private double fear;
                private double happiness;

                public double getSadness() {
                    return sadness;
                }

                public void setSadness(double sadness) {
                    this.sadness = sadness;
                }

                public double getNeutral() {
                    return neutral;
                }

                public void setNeutral(double neutral) {
                    this.neutral = neutral;
                }

                public double getDisgust() {
                    return disgust;
                }

                public void setDisgust(double disgust) {
                    this.disgust = disgust;
                }

                public double getAnger() {
                    return anger;
                }

                public void setAnger(double anger) {
                    this.anger = anger;
                }

                public double getSurprise() {
                    return surprise;
                }

                public void setSurprise(double surprise) {
                    this.surprise = surprise;
                }

                public double getFear() {
                    return fear;
                }

                public void setFear(double fear) {
                    this.fear = fear;
                }

                public double getHappiness() {
                    return happiness;
                }

                public void setHappiness(double happiness) {
                    this.happiness = happiness;
                }
            }

            public static class GenderBean {
                /**
                 * value : Female
                 */

                private String value;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }

            public static class AgeBean {
                /**
                 * value : 24
                 */

                private int value;

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }

            public static class SmileBean {
                /**
                 * threshold : 50
                 * value : 0.104
                 */

                private int threshold;
                private double value;

                public int getThreshold() {
                    return threshold;
                }

                public void setThreshold(int threshold) {
                    this.threshold = threshold;
                }

                public double getValue() {
                    return value;
                }

                public void setValue(double value) {
                    this.value = value;
                }
            }

            public static class FacequalityBean {
                /**
                 * threshold : 70.1
                 * value : 0.006
                 */

                private double threshold;
                private double value;

                public double getThreshold() {
                    return threshold;
                }

                public void setThreshold(double threshold) {
                    this.threshold = threshold;
                }

                public double getValue() {
                    return value;
                }

                public void setValue(double value) {
                    this.value = value;
                }
            }

            public static class EthnicityBean {
                /**
                 * value : WHITE
                 */

                private String value;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }

            public static class BeautyBean {
                /**
                 * female_score : 89.59
                 * male_score : 87.166
                 */

                private double female_score;
                private double male_score;

                public double getFemale_score() {
                    return female_score;
                }

                public void setFemale_score(double female_score) {
                    this.female_score = female_score;
                }

                public double getMale_score() {
                    return male_score;
                }

                public void setMale_score(double male_score) {
                    this.male_score = male_score;
                }
            }
        }
    }
}

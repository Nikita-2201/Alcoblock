package ru.gknsv;

import javafx.scene.image.Image;
import ru.gknsv.dto.AlcoHistoryDto;
import ru.gknsv.dto.RealImageDto;
import ru.gknsv.dto.UserDto;
import ru.gknsv.dto.AlcoDto;

import java.util.ArrayList;
import java.util.HashMap;

public class Model {

    private HashMap<String, Image> conditionImages;
    private double amountAlcoholInBlood;
    private double volumeOfAlcohol;
    private double alcoholStrength;
    private double height;
    private double weight;
    private double numberOfDrunksPerMonth;
    private double sex;
    private boolean isHungry;
    private ArrayList<AlcoHistoryDto> alcoList;
    private UserDto sessionUser;
    private ArrayList<RealImageDto> realImageList;

    public Model() {
        conditionImages = new HashMap<>();
        for (int i = 1; i < 6; i++) {
            Image image = new Image("ru/gknsv/human_" + i + ".png");
            conditionImages.put("human_" + i, image);
        }
    }

    public void setSessionUser(UserDto sessionUser) {
        this.sessionUser = sessionUser;
        setHeight(sessionUser.getHeight());
        setWeight(sessionUser.getWeight());
        setNumberOfDrunksPerMonth(sessionUser.getExperience());
        setSex(sessionUser.getSex());
        amountAlcoholInBlood = 0;
    }

    public ArrayList<RealImageDto> getRealImageList() {
        if (realImageList == null) {
            realImageList = new ArrayList<>();
        }
        return realImageList;
    }

    public boolean containPicture(String pictureId) {
        boolean flag = false;
        if (realImageList != null) {
            for (RealImageDto realImageDto : realImageList) {
                if (realImageDto.getId().equals(pictureId)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public RealImageDto getRealImageDto(String pictureId) {
        RealImageDto realImageDto = null;
        for (RealImageDto o : realImageList) {
            if (o.getId().equals(pictureId)) {
                realImageDto = o;
                break;
            }
        }
        return realImageDto;
    }

    public UserDto getSessionUser() {
        return sessionUser;
    }

    public ArrayList<AlcoHistoryDto> getAlcoList() {
        return alcoList;
    }

    public void addAclo(AlcoHistoryDto alcoDto, long interval) {
        if (alcoList == null) {
            alcoList = new ArrayList<>();
            interval = 0;
        }
        alcoList.add(alcoDto);
        volumeOfAlcohol = Double.parseDouble(alcoDto.getVolume());
        alcoholStrength = Double.parseDouble(alcoDto.getStrength());
        updateAmountAlcohol(interval);
        amountAlcoholInBlood += (volumeOfAlcohol * getConvertedHungry() * (alcoholStrength / 100) * height) / (weight * sex * numberOfDrunksPerMonth);
    }

    public void setHungry(boolean isHungry) {
        this.isHungry = isHungry;
    }

    public boolean getHungry() {
        return isHungry;
    }

    public void setHeight(String input) {
        double convertedInput = Double.parseDouble(input);
        if (convertedInput > 140.0) {
            if (convertedInput > 160.0) {
                if (convertedInput > 180.0) {
                    height = 0.75;
                } else height = 0.8;
            } else height = 0.9;
        } else height = 1;
    }

    public void setWeight(String input) {
        weight = Double.parseDouble(input);
    }

    public void setNumberOfDrunksPerMonth(String input) {
        double convertedInput = Double.parseDouble(input);
        if (convertedInput <= 3.0) {
            convertedInput = 0.0;
        } else if (convertedInput > 3.0 && convertedInput <= 7.0) {
            convertedInput = 5.0;
        }
        numberOfDrunksPerMonth = convertedInput * 0.7 / 30.0;
        if (numberOfDrunksPerMonth >= 0.6) {
            numberOfDrunksPerMonth = 1.6;
        } else if (numberOfDrunksPerMonth < 0.6) {
            numberOfDrunksPerMonth = numberOfDrunksPerMonth + 1.0;
        }
    }

    public void setSex(String input) {
        if (sessionUser.getSex().equals("male")) {
            sex = 0.7;
        } else {
            sex = 0.6;
        }
    }

    public double getConvertedHungry() {
        if (isHungry) {
            return 0.8056;
        } else {
            return 1;
        }
    }

    public void updateAmountAlcohol(long interval) {
        double intervalInHours = ((double) interval) / 3600000.0;
        double C1 = intervalInHours * 0.15;

        if (amountAlcoholInBlood >= C1) {
            amountAlcoholInBlood = amountAlcoholInBlood - C1;
        } else if (amountAlcoholInBlood < C1) {
            amountAlcoholInBlood = 0;
        }

    }

    public String getAlcoholWeatheringTime() {
        double resultTime = amountAlcoholInBlood / 0.15;
        int hours = (int) resultTime % 100;
        int minutes = (int) ((resultTime % 1) * 60);
        return hours + " ч. " + minutes + " мин";
    }

    public String getBloodAlcoholConcentration() {
        int miles = (int) amountAlcoholInBlood % 10;
        int promiles = (int) ((amountAlcoholInBlood % 1) * 100);
        return miles + "," + promiles;
    }

    public String getScore() {
        String result = "";

        if (amountAlcoholInBlood >= 5.0) {
            result = " Риск сильнейшего отравления и летального исхода (Превышен лимит оптимальной дозы алкоголя в крови, необходимо прекратить употребление алкоголя).";
        } else if (amountAlcoholInBlood >= 4.0 && amountAlcoholInBlood < 5.0) {
            result = " Полная потеря контроля за своим поведением, потеря сознания, риск смерти\n(нарушение" +
                    " дыхания, сердцебиения, нистагм) (Превышен лимит оптимальной дозы алкоголя в крови, необходимо прекратить употребление алкоголя).";
        } else if (amountAlcoholInBlood >= 3.0 && amountAlcoholInBlood < 4.0) {
            result = " Сильнейшее угнетение ЦНС, потеря сознания (неконтролируемое мочеиспускание,\nутрата" +
                    " чувства равновесия, нарушение сердцебиения и дыхания), возникает\nугроза смерти (Превышен лимит оптимальной дозы алкоголя в крови, необходимо прекратить употребление алкоголя).";
        } else if (amountAlcoholInBlood >= 2.0 && amountAlcoholInBlood < 3.0) {
            result = " Теряется понимание, состояние ступора, потеря ощущений, возможно, потеря\nсознания," +
                    " памяти, нарушение моторики (Превышен лимит оптимальной дозы алкоголя в крови, необходимо прекратить употребление алкоголя).";
        } else if (amountAlcoholInBlood >= 1.0 && amountAlcoholInBlood < 2.0) {
            result = " Экспрессивность, необоснованная агрессия, эмоциональная лабильность,\n" +
                    "нарушение равновесия (шатающаяся походка), нечленораздельная речь,\nнарушение " +
                    "рефлексов, замедленная реакция (Превышен лимит оптимальной дозы алкоголя в крови, необходимо прекратить употребление алкоголя).";
        } else if (amountAlcoholInBlood >= 0.6 && amountAlcoholInBlood < 1.0) {
            result = " Экстравертность, расторможенность, неестественное веселье, притупление\nощущений," +
                    " нарушается периферическое зрение, глубина восприятия, рассуждения\nстановятся бессвязными (Превышен лимит оптимальной дозы алкоголя в крови, необходимо прекратить употребление алкоголя).";
        } else if (amountAlcoholInBlood >= 0.3 && amountAlcoholInBlood < 0.6) {
            result = " Легкая эйфория, чувство расслабления, радости, повышенная общительность," +
                    "\nразговорчивость, нарушение концентрации внимания (Оптимальная доза потребления алкоголя - рекомендуется больше не употреблять алкоголь).";
        } else if (amountAlcoholInBlood >= 0.1 && amountAlcoholInBlood < 0.3) {
            result = " Состояние организма в целом считается нормальным, наличие алкоголя в крови определяется только" +
                    "\nспециальными приборами (Можно еще немного выпить).";
        } else if (amountAlcoholInBlood < 0.1) {
            result = " Трезвый.";
        }
        return result;
    }

    public void clearAmountAlcoholInBlood() {
        amountAlcoholInBlood = 0;
    }

    public void clearAlcoList() {
        alcoList = null;
    }

    public Image getConditionImage() {
        Image result = null;
        if (amountAlcoholInBlood >= 5.0) {
            result = conditionImages.get("human_5");
        } else if (amountAlcoholInBlood >= 3.0 && amountAlcoholInBlood < 5.0) {
            result = conditionImages.get("human_4");
        } else if (amountAlcoholInBlood >= 1.0 && amountAlcoholInBlood < 3.0) {
            result = conditionImages.get("human_3");
        } else if (amountAlcoholInBlood >= 0.3 && amountAlcoholInBlood < 1.0) {
            result = conditionImages.get("human_2");
        } else if (amountAlcoholInBlood < 0.3) {
            result = conditionImages.get("human_1");
        }
        return result;
    }
}

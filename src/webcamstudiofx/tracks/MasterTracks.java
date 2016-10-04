/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.tracks;

import java.util.ArrayList;
import webcamstudiofx.streams.SourceTrack;
import webcamstudiofx.streams.Stream;
import webcamstudiofx.util.Tools;

/**
 *
 * @author patrick (modified by karl)
 */
public class MasterTracks {

    static MasterTracks instance = null;

    public static MasterTracks getInstance() {
        if (instance == null) {
            instance = new MasterTracks();
        }
        return instance;
    }
    ArrayList<String> trackNames = new ArrayList<>();
    ArrayList<Stream> streams = new ArrayList<>();
    int rmAddIndex = 0;
    ArrayList<SourceTrack> tempSC = null;

    private MasterTracks() {
    }

    public void register(Stream s) {
        String streamName = s.getClass().getName();
        streamName = streamName.replace("webcamstudiofx.streams.", "");
        if (!s.getClass().toString().contains("Sink")) {
//            System.out.println(streamName + " registered.");
        }
        streams.add(s);
    }

    public void unregister(Stream s) {
        if (!s.getClass().toString().contains("Sink")) {
//            System.out.println(s.getName() + " unregistered.");
        }
        streams.remove(s);
    }

    public void addTrack(String name) {
        trackNames.add(name);
        streams.stream().forEach((s) -> {
            s.addTrack(SourceTrack.getTrack(name, s));
        });
    }

    public void addTrack2List(String name) {
        trackNames.add(name);
    }

    public void addPlayTrack(String name, String playTrk) {
        trackNames.add(name);
        streams.stream().forEach((s) -> {
            if (playTrk.contains(s.getTrkName())) {
                s.setIsPlaying(false);
                s.addTrack(SourceTrack.getTrack(name, s));
                s.setIsPlaying(true);
            } else {
                s.addTrack(SourceTrack.getTrack(name, s));
            }
        });
    }

    public static SourceTrack getTrack(String trackName, Stream s) {
        SourceTrack track = null;
        for (SourceTrack tr : s.getTracks()) {
            if (tr.getName().equals(trackName)) {
                track = tr;
            }
        }
        return track;
    }

    public void addTrkTransitions(String name) {
        streams.stream().forEach((s) -> {
            s.getTracks().stream().filter((sc) -> (!sc.getName().equals(name))).map((sc) -> {
                //                    System.out.println("Adding to channel: "+sc.getName());
                sc.startTransitions.clear();
                return sc;
            }).map((sc) -> {
                sc.startTransitions.addAll(s.getStartTransitions());
                return sc;
            }).map((sc) -> {
                sc.endTransitions.clear();
                return sc;
            }).forEach((sc) -> {
                sc.endTransitions.addAll(s.getEndTransitions());
            });
        });
    }

    public void addToTracks(String name) {
        trackNames.add(name);

    }

    public void addTrackAt(String name, int index) {
        trackNames.add(index, name);

    }

    public void updateTrack(String name) {
        for (Stream s : streams) {
            String streamName = s.getClass().getName();
            SourceTrack sc = null;
            ArrayList<SourceTrack> sourceCh = s.getTracks();
            int x = 0;
            for (int i = 0; i < sourceCh.size(); i++) {
                if (sourceCh.get(i).getName().equals(name)) {
                    sc = sourceCh.get(i);
                    x = i;
                    break;
                }
            }
            if (!streamName.contains("Sink")) {
                if (s.getisATrack()) {
                    boolean isP = s.isPlaying();
                    if (sc != null) {
                        s.setIsPlaying(sc.getIsPlaying());
                        s.removeTrackAt(x);
                    }
                    s.addTrackAt(SourceTrack.getTrack(name, s), x);
                    s.setIsPlaying(isP);
                } else {
                    if (sc != null) {
                        s.removeTrackAt(x);
                    }
                    s.addTrackAt(SourceTrack.getTrack(name, s), x);
                }
                x = 0;
            }
        }
    }

    public void insertStudio(String name) {
        streams.stream().forEach((s) -> {
            int co = 0;
            co = s.getTracks().stream().filter((ssc) -> (ssc.getName().equals(name))).map((_item) -> 1).reduce(co, Integer::sum);
            if (co == 0) {
                if (s.getisATrack()) {
                    boolean backState = false;
                    if (s.isPlaying()) {
                        s.setIsPlaying(false);
                        backState = true;
                    }
                    s.addTrack(SourceTrack.getTrack(name, s));
                    if (backState) {
                        s.setIsPlaying(true);
                    }
                } else {
                    boolean backState = false;
                    if (s.isPlaying()) {
                        backState = true;
                    }
                    s.addTrack(SourceTrack.getTrack(name, s));
                    if (backState) {
                        s.setIsPlaying(true);
                    } else {
                        s.setIsPlaying(false);
                    }
                }
            } else {
                ArrayList<String> allChan = new ArrayList<>();
                MasterTracks.getInstance().getTracks().stream().forEach((scn) -> {
                    allChan.add(scn);
                });
                s.getTracks().stream().map((scc3) -> scc3.getName()).forEach((removech) -> {
                    allChan.remove(removech);
                });
                allChan.stream().forEach((ssc2) -> {
                    s.addTrack(SourceTrack.getTrack(ssc2, s));
                });
            }
        });
    }

    public void removeTrack(String name) {
        trackNames.remove(name);
        streams.stream().forEach((s) -> {
            SourceTrack toRemove = null;
            for (SourceTrack sc : s.getTracks()) {
                if (sc.getName().equals(name)) {
                    toRemove = sc;
                }
            }
            if (toRemove != null) {
                s.removeTrack(toRemove);
            }
        });
    }

    public void removeTrackAt(String name) {
        trackNames.remove(name);

    }

    public void removeTrackIndex(int index) {
        trackNames.remove(index);
    }

    public void selectTrack(String name) {
        for (Stream stream : streams) {
            for (SourceTrack sc : stream.getTracks()) {
                if (sc.getName().equals(name)) {
                    sc.apply(stream);
                    break;
                }
            }
        }
    }

    public ArrayList<String> getTracks() {
        return trackNames;
    }

    public void stopAllStream() {
        streams.stream().forEach((s) -> {
            //            System.out.println("Stream stopped: "+s);
            if (s.isPlaying()) {
                if (s.getLoop()) {
                    s.setLoop(false);
                    Tools.sleep(30);
                    s.stop();
                    s.setLoop(true);
                } else {
                    Tools.sleep(30);
                    s.stop();
                }
            }
        });
    }

    public void endAllStream() {
        streams.stream().forEach((s) -> {
            if (s.isPlaying()) {
                if (s.getLoop()) {
                    s.setLoop(false);
                    Tools.sleep(30);
                    s.stop();
                } else {
                    Tools.sleep(30);
                    s.stop();
                }
            }
        });
    }

    public void stopTextCDown() {
        streams.stream().forEach((s) -> {
            String streamName = s.getClass().getName();
            if (!streamName.contains("Sink")) {
                if (streamName.endsWith("SourceText")) {
                    if (s.getIsACDown()) {
                        s.stop();
                        s.updateStatus();
                    }
                }
            }
        });
    }

    public void stopOnlyStream() {
        streams.stream().forEach((s) -> {
            String streamName = s.getClass().getName();
            if (!streamName.contains("Sink")) {
                if (s.isPlaying()) {
                    if (s.getLoop()) {
                        s.setLoop(false);
                        Tools.sleep(30);
                        s.stop();
                        s.setLoop(true);
                    } else {
                        Tools.sleep(30);
                        s.stop();
                    }
                }
            }
        });
    }

    public ArrayList<Stream> getStreams() {
        return streams;
    }
}

package net.povstalec.sgjourney.common.capabilities;

public interface IBloodstreamNaquadah {
    public boolean hasNaquadahInBloodstream();

    public void addNaquadahToBloodstream();

    public void removeNaquadahFromBloodstream();

    public void copyFrom(IBloodstreamNaquadah source);
}
